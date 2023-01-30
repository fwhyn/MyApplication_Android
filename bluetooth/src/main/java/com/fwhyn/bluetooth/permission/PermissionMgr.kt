package com.fwhyn.bluetooth.permission

interface PermissionMgr{
    fun onPermissionGranted()
    fun onRequestRationale(permissions: Map<String, Boolean>)
    fun onPermissionDenied(permissions: Map<String, Boolean>)
}