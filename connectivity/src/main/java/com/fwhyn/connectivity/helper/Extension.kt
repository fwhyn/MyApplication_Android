package com.fwhyn.connectivity.helper

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.util.Log

class Extension

val TAG: String = "fwhyn_test_" + Extension::class.java.simpleName

fun BluetoothLeScanner.startScanning(callback: ScanCallback) {
    Log.d(TAG, "BluetoothLeScanner startScanning invoked")
    this.startScan(callback)
}

fun BluetoothLeScanner.stopScanning(callback: ScanCallback) {
    Log.d(TAG, "BluetoothLeScanner stopScanning invoked")
    this.stopScan(callback)
}