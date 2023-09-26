package com.fwhyn.bluetooth.permission

interface PermissionCheckCallback {

    fun onPermissionGranted()

    fun onRequestRationale(rationalePermissions: Array<String>)

    fun onPermissionDenied(deniedPermissions: Array<String>)
}