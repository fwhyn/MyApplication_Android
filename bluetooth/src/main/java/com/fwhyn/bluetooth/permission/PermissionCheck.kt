package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.pm.PackageManager

// Singleton, activity from Application
class PermissionCheck(private val activity: Activity) {
    private lateinit var permissionMap: HashMap<String, Boolean>

    fun permissionsCheck(permissions: Array<String>, permissionMgr: PermissionMgr) {
        when {
            permissionsGranted(permissions) -> {
                permissionMgr.onPermissionGranted()
            }

            shouldShowRequestPermissionsRationale(permissions) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                permissionMgr.onRequestRationale(permissionMap)
            }

            else -> {
                // Other conditions
                // change to request permission
                permissionMgr.onPermissionDenied(permissionMap)
            }
        }
    }

    // --------------------------------
    private fun permissionsGranted(permissions: Array<String>): Boolean {
        return loopPermissionCheck(permissions) {
            permissionGranted(it)
        }
    }

    private fun shouldShowRequestPermissionsRationale(permissions: Array<String>): Boolean {
        return loopPermissionCheck(permissions) {
            activity.shouldShowRequestPermissionRationale(it)
        }
    }

    private fun permissionGranted(permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun loopPermissionCheck(permissions: Array<String>, callback: (String) -> Boolean): Boolean {
        var retVal = true
        permissionMap = HashMap()

        for (permission in permissions) {
            if (!callback(permission)) {
                retVal = false
                permissionMap[permission] = false
            }
        }

        return retVal
    }

    // --------------------------------
    enum class Permission {
        NEED_RATIONALE, NOT_GRANTED, GRANTED
    }
}