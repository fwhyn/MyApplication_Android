package com.fwhyn.connectivity.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Log

class Extension

val TAG: String = "fwhyn_test_" + Extension::class.java.simpleName

fun Activity.getBtAdapterOrNull(): BluetoothAdapter? {
    val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager ?: kotlin.run {
        Log.e(TAG, "Bluetooth Manager not found")
        null
    }
    return bluetoothManager?.adapter ?: kotlin.run {
        Log.e(TAG, "Bluetooth Adapter not found")
        null
    }
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Intent.getParcelable(key: String, cls: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(key, cls)
    } else {
        this.getParcelableExtra(key) as? T
    }
}