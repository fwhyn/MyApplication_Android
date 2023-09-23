package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionRequest(
    activityResultCaller: ActivityResultCaller,
    requestPermissionResult: PermissionRequestCallback
) {

    private val launcher: ActivityResultLauncher<Array<String>> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            requestPermissionResult.onFinishedRequest(results)
        }

    fun requestPermissions(permissions: Array<String>) {
        launcher.launch(permissions)
    }

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