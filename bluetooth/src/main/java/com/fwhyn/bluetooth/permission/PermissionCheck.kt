package com.fwhyn.bluetooth.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionCheck(private val activity: Activity) {

    fun permissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRequestPermissionRationale(permission: String) {
        activity.shouldShowRequestPermissionRationale(permission)
    }

    fun permissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!permissionGranted(permission)) return false
        }

        return true
    }

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val value: T) : Result<T>()
        data class Failure<out T : Any>(val throwable: Throwable) : Result<T>()
    }
}