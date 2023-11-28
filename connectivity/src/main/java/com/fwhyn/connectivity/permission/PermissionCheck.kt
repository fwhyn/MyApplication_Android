package com.fwhyn.connectivity.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

class PermissionCheck {

    companion object {
        fun checkPermissions(
            activity: Activity,
            permissions: Array<String>,
            permissionCallback: PermissionCheckCallback
        ) {
            when (val permissionResult: PermissionResult = getPermissionResult(activity, permissions)) {
                is PermissionResult.Denied -> permissionCallback.onPermissionDenied(permissionResult.deniedPermissions)
                is PermissionResult.NeedRationale -> permissionCallback.onRequestRationale(permissionResult.rationalePermissions)
                PermissionResult.Granted -> permissionCallback.onPermissionGranted()
            }
        }

        // --------------------------------
        private fun getPermissionResult(activity: Activity, permissions: Array<String>): PermissionResult {
            val deniedPermissions = ArrayList<String>()
            val rationalePermissions = ArrayList<String>()

            val remainPossiblyDeniedPermissions = ArrayList(permissions.toList())
            for (permission in permissions) {
                if (shouldShowRequestPermissionsRationale(activity, permission)) {
                    rationalePermissions.add(permission)
                    remainPossiblyDeniedPermissions.remove(permission)
                }
            }

            for (permission in remainPossiblyDeniedPermissions) {
                if (permissionDenied(activity, permission)) {
                    deniedPermissions.add(permission)
                }
            }

            return when {
                deniedPermissions.isNotEmpty() -> PermissionResult.Denied(deniedPermissions.toTypedArray())
                rationalePermissions.isNotEmpty() -> PermissionResult.NeedRationale(rationalePermissions.toTypedArray())
                else -> PermissionResult.Granted
            }
        }

        private fun shouldShowRequestPermissionsRationale(activity: Activity, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.shouldShowRequestPermissionRationale(permission)
            } else {
                false
            }
        }

        private fun permissionDenied(activity: Activity, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED
            } else {
                false
            }
        }
    }

    // --------------------------------
    sealed class PermissionResult {
        data object Granted : PermissionResult()
        class NeedRationale(val rationalePermissions: Array<String>) : PermissionResult()
        class Denied(val deniedPermissions: Array<String>) : PermissionResult()
    }
}