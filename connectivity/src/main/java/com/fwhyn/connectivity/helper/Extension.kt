package com.fwhyn.connectivity.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
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