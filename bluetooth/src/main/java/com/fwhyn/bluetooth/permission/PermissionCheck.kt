package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.pm.PackageManager

abstract class PermissionCheck {
    private val deniedPermissions = ArrayList<String>()

    protected fun checkPermissions(
        activity: Activity,
        permissions: Array<String>,
        permissionCallback: PermissionCallback
    ) {
        when {
            shouldShowRequestPermissionsRationale(activity, permissions) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                permissionCallback.onRequestRationale(deniedPermissions.toTypedArray())
            }

            permissionsGranted(activity, permissions) -> {
                permissionCallback.onPermissionGranted()
            }


            else -> {
                // Other conditions
                // change to request permission
                permissionCallback.onPermissionDenied(deniedPermissions.toTypedArray())
            }
        }
    }

    // --------------------------------
    private fun shouldShowRequestPermissionsRationale(activity: Activity, permissions: Array<String>): Boolean {
        val result = deniedPermissionExist(permissions) {
            !activity.shouldShowRequestPermissionRationale(it)
        }
        return result
    }

    private fun permissionsGranted(activity: Activity, permissions: Array<String>): Boolean {
        val result = deniedPermissionExist(permissions) {
            permissionGranted(activity, it)
        }
        return !result
    }

    private fun permissionGranted(activity: Activity, permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun deniedPermissionExist(permissions: Array<String>, isGranted: (String) -> Boolean): Boolean {
        var retVal = false
        deniedPermissions.clear()

        for (permission in permissions) {
            if (!isGranted(permission)) {
                deniedPermissions.add(permission)
                retVal = true
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
        fun onRequestRationale(permissions: Array<String>)
        fun onPermissionDenied(permissions: Array<String>)
    }
}