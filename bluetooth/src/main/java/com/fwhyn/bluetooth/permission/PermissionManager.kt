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
class PermissionManager(
    private val activity: Activity,
    activityResultCaller: ActivityResultCaller,
    private val permissionCallback: PermissionCheckCallback,
    private val permissions: Array<String>
) : PermissionCheckCallback, PermissionRequestCallback {

    private val permissionRequest = PermissionRequest(activityResultCaller, this)

    fun checkOrRequestPermissions() {
        PermissionCheck.checkPermissions(activity, permissions, this)
    }

    // --------------------------------
    override fun onPermissionGranted() {
        permissionCallback.onPermissionGranted()
    }

    override fun onRequestRationale(rationalePermissions: Array<String>) {
        permissionCallback.onRequestRationale(rationalePermissions)
    }

    override fun onPermissionDenied(deniedPermissions: Array<String>) {
        permissionRequest.requestPermissions(permissions)
    }

    override fun onFinishedRequest(results: Map<String, Boolean>) {
        PermissionCheck.checkPermissions(activity, permissions, permissionCallback)
    }
}