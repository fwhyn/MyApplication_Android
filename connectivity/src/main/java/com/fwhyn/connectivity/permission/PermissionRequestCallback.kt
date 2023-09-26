package com.fwhyn.connectivity.permission

interface PermissionRequestCallback {

    fun onFinishedRequest(results: Map<String, Boolean>)
}