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

annotation class SuppressWarning

@SuppressWarning
class PermissionManager(
    private val componentActivity: ComponentActivity,
    private val permissionCallback: PermissionCheckCallback,
) {

    private lateinit var permissions: Array<String>

    companion object {

        fun openPermissionSetting(activity: Activity) {
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

    private val permissionRequest = PermissionRequest(
        componentActivity,
        object : PermissionRequestCallback {
            override fun onFinishedRequest(results: Map<String, Boolean>) {
                PermissionCheck.checkPermissions(componentActivity, permissions, permissionCallback)
            }
        }
    )

    fun checkOrRequestPermissions(permissions: Array<String>) {
        this.permissions = permissions
        PermissionCheck.checkPermissions(
            componentActivity,
            permissions,
            object : PermissionCheckCallback {
                override fun onPermissionGranted() {
                    permissionCallback.onPermissionGranted()
                }

                override fun onRequestRationale(rationalePermissions: Array<String>) {
                    permissionCallback.onRequestRationale(rationalePermissions)
                }

                override fun onPermissionDenied(deniedPermissions: Array<String>) {
                    permissionRequest.requestPermissions(permissions)
                }
            })
    }
}