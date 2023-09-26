package com.fwhyn.connectivity.permission

interface PermissionCheckCallback {

    fun onPermissionGranted()

    fun onRequestRationale(rationalePermissions: Array<String>)

    fun onPermissionDenied(deniedPermissions: Array<String>)
}