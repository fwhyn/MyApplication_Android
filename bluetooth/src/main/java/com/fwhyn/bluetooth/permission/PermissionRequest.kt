package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

abstract class PermissionRequest(activityResultCaller: ActivityResultCaller) : PermissionCheck() {

    private lateinit var callback: RequestPermissionResult

    private val launcher: ActivityResultLauncher<Array<String>> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            callback.onFinished(results)
        }

    protected fun requestPermissions(permissions: Array<String>, callback: RequestPermissionResult) {
        this.callback = callback
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

    interface RequestPermissionResult {
        fun onFinished(results: Map<String, Boolean>)
    }
}