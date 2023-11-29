package com.fwhyn.connectivity.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast


/**
 * Activity for scanning and displaying available BLE devices.
 */
class BleScan(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter(),
) {

    private var mScanning: Boolean = false
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    @SuppressLint("MissingPermission")
    fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    mScanning = false
                    bluetoothLeScanner.stopScan(leScanCallback)

                    Toast.makeText(context, "Scanning stopped found", Toast.LENGTH_SHORT).show()
                }, SCAN_PERIOD)
                mScanning = true
                bluetoothLeScanner.startScan(leScanCallback)

                Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
            }

            else -> {
                mScanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
            }
        }
    }

//    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
//        // Toast.makeText(context, "BLE found", Toast.LENGTH_SHORT).show()
//        Log.d(TAG, "BLE found")
//    }

    //     private val leDeviceListAdapter = LeDeviceListAdapter()
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            Log.d(TAG, "BLE found: ${result.device}")
//            leDeviceListAdapter.addDevice(result.device)
//            leDeviceListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("MissingPermission")
    fun connectDevice(context: Context, device: BluetoothDevice) {
        val gatt: BluetoothGatt = device.connectGatt(context, false, gattCallback)
        gatt.discoverServices()

//        val service = gatt.getService(serviceUuid)
//        val characteristic = service.getCharacteristic(characteristicUuid)
    }

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            // Handle connection state changes here
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            // Handle characteristic reads here
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            // Handle characteristic writes here
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            // Handle characteristic changes here
        }
    }

    companion object {
        private val TAG: String = "fwhyn_test_" + BleScan::class.java.simpleName
        private const val SCAN_PERIOD: Long = 10000
    }
}