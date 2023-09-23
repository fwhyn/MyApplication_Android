package com.fwhyn.bluetooth.permission

interface PermissionRequestCallback {

    fun onFinishedRequest(results: Map<String, Boolean>)
}