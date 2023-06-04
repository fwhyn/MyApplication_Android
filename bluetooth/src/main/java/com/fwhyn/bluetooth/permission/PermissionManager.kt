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

                override fun onRequestRationale(rationalePermissions: Array<String>) {
                    permissionCallback.onRequestRationale(permissions)
                }

                override fun onPermissionDenied(deniedPermissions: Array<String>) {
                    requestPermissions(
                        deniedPermissions,
                        object : RequestPermissionResult {
                            override fun onFinished(results: Map<String, Boolean>) {
                                // check all permissions again, not from the results
                                checkPermissions(activity, permissions, permissionCallback)
                            }
                        }
                    )
                }

            }
        )
    }
}