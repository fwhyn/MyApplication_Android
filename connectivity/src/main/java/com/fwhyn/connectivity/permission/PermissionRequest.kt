package com.fwhyn.connectivity.permission

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
}