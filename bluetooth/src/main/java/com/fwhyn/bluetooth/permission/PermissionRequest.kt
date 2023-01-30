package com.fwhyn.bluetooth.permission

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionRequest {

    /**
     * Please register the launcher before activity created
     */
    fun registerLauncher(activity: ActivityResultCaller, callback: (Boolean) -> Unit) : ActivityResultLauncher<String> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            callback(isGranted)
        }
    }

    fun requestPermission(permission: String, launcher: ActivityResultLauncher<String>) {
        launcher.launch(permission)
    }
}