package com.fwhyn.bluetooth.permission

class PermissionManager {

    fun accessFeature() {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        when {
            shouldShowRequestPermissionRationale(btConnect) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                // showInContextUI()
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(btConnect)
            }
        }
    }
}