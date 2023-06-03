package com.fwhyn.bluetooth.permission

import android.app.Activity
import androidx.activity.result.ActivityResultCaller

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "Please create the object before the activity created"
)

annotation class WarningMessage

@WarningMessage
open class PermissionManager(activityResultCaller: ActivityResultCaller) : PermissionRequest(activityResultCaller) {
    fun checkOrRequestPermissions(
        activity: Activity,
        permissions: Array<String>,
        permissionCallback: PermissionCallback
    ) {
        checkPermissions(
            activity,
            permissions,
            object : PermissionCallback {
                override fun onPermissionGranted() {
                    permissionCallback.onPermissionGranted()
                }

                override fun onRequestRationale(permissions: Array<String>) {
                    permissionCallback.onRequestRationale(permissions)
                }

                override fun onPermissionDenied(permissions: Array<String>) {
                    requestPermissions(
                        permissions,
                        object : RequestPermissionResult {
                            override fun onFinished(results: Map<String, Boolean>) {
                                val deniedPermissions = getDeniedPermissions(permissions, results)

                                if (deniedPermissions.isEmpty()) {
                                    permissionCallback.onPermissionGranted()
                                } else {
                                    // checkPermissions(activity, deniedPermissions, permissionCallback)
                                    permissionCallback.onPermissionDenied(deniedPermissions)
                                }
                            }
                        }
                    )
                }

            }
        )
    }

    private fun getDeniedPermissions(permissions: Array<String>, results: Map<String, Boolean>): Array<String> {
        val deniedPermissions = ArrayList<String>()

        permissions.map { permission ->
            val isGranted = results[permission]
            isGranted?.let {
                if (!it) deniedPermissions.add(permission)
            }
        }

        return deniedPermissions.toTypedArray()
    }
}