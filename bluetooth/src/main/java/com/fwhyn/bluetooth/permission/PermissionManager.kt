package com.fwhyn.bluetooth.permission

import androidx.activity.result.ActivityResultCaller

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "Please create the object before the activity created"
)

annotation class WarningMessage

@WarningMessage
open class PermissionManager(activityResultCaller: ActivityResultCaller) : PermissionCheck(activityResultCaller)