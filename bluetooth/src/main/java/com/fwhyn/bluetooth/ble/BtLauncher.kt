package com.fwhyn.bluetooth.ble

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

data class BtLauncher (
    val enableBtLauncher: ActivityResultLauncher<Intent>,
    val permissionLauncher: ActivityResultLauncher<String>
    )