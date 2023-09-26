package com.fwhyn.connectivity.ble

import android.bluetooth.BluetoothAdapter
import android.os.Handler

/**
 * Activity for scanning and displaying available BLE devices.
 */
class BleScan(
    private val bluetoothAdapter: BluetoothAdapter,
    private val handler: Handler
) {

    private var mScanning: Boolean = false

    private fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    mScanning = false
                    bluetoothAdapter.stopLeScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                bluetoothAdapter.startLeScan(leScanCallback)
            }
            else -> {
                mScanning = false
                bluetoothAdapter.stopLeScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->

    }

    companion object {
        private const val SCAN_PERIOD: Long = 10000
    }
}