package com.fwhyn.connectivity.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "Please create the object before the activity created"
)
annotation class PermissionManagerWarning

class PermissionManager private constructor(
    private val activity: ComponentActivity,
    private val callback: PermissionCheckCallback,
) {

    companion object {

        @PermissionManagerWarning
        fun getInstance(
            componentActivity: ComponentActivity,
            permissionCallback: PermissionCheckCallback,
        ): PermissionManager {
            return PermissionManager(
                componentActivity,
                permissionCallback,
            )
        }

        fun openSetting(activity: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.run {
                data = Uri.fromParts("package", activity.packageName, null)
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }

            activity.startActivity(intent)
        }
    }

    private lateinit var permissions: Array<String>
    private val permissionRequest = PermissionRequest(
        activity,
        object : PermissionRequestCallback {
            override fun onFinishedRequest(results: Map<String, Boolean>) {
                PermissionCheck.checkPermissions(activity, permissions, callback)
            }
        }
    )

    fun checkOrRequestPermissions(permissions: Array<String>) {
        this.permissions = permissions
        PermissionCheck.checkPermissions(
            activity,
            permissions,
            object : PermissionCheckCallback {
                override fun onPermissionGranted() {
                    callback.onPermissionGranted()
                }

                override fun onRequestRationale(rationalePermissions: Array<String>) {
                    callback.onRequestRationale(rationalePermissions)
                }

                override fun onPermissionDenied(deniedPermissions: Array<String>) {
                    permissionRequest.requestPermissions(permissions)
                }
            })
    }
}