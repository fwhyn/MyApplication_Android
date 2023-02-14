package com.fwhyn.bluetooth.permission

interface PermissionMgr{
    fun onPermissionGranted()
    fun onRequestRationale(permissions: List<String>)
    fun onPermissionDenied(permissions: List<String>)
}