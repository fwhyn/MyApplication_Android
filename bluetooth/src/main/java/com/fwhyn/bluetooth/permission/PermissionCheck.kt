package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller

abstract class PermissionCheck(activityResultCaller: ActivityResultCaller) : PermissionRequest(activityResultCaller) {
    private lateinit var deniedPermissions: ArrayList<String>

    fun checkPermissions(
        activity: Activity,
        permissions: Array<String>,
        permissionCallback: PermissionCallback
    ) {
        when {
            permissionsGranted(activity, permissions) -> {
                permissionCallback.onPermissionGranted()
            }

            shouldShowRequestPermissionsRationale(activity, permissions) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                permissionCallback.onRequestRationale(deniedPermissions)
            }

            else -> {
                // Other conditions
                // change to request permission
                permissionCallback.onPermissionDenied(deniedPermissions)
            }
        }
    }

    // --------------------------------
    private fun permissionsGranted(activity: Activity, permissions: Array<String>): Boolean {
        return loopPermissionCheck(permissions) {
            permissionGranted(activity, it)
        }
    }

    private fun shouldShowRequestPermissionsRationale(activity: Activity, permissions: Array<String>): Boolean {
        return loopPermissionCheck(permissions) {
            activity.shouldShowRequestPermissionRationale(it)
        }
    }

    private fun permissionGranted(activity: Activity, permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun loopPermissionCheck(permissions: Array<String>, callback: (String) -> Boolean): Boolean {
        var retVal = true
        deniedPermissions = ArrayList()

        for (permission in permissions) {
            if (!callback(permission)) {
                deniedPermissions.add(permission)
                retVal = false
            }
        }

        return retVal
    }

    // --------------------------------
    enum class Permission {
        NEED_RATIONALE, NOT_GRANTED, GRANTED
    }

    interface PermissionCallback {
        fun onPermissionGranted()
        fun onRequestRationale(permissions: List<String>)
        fun onPermissionDenied(permissions: List<String>)
    }
}