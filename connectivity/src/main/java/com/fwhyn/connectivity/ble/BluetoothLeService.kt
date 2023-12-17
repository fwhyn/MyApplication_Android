package com.fwhyn.connectivity.ble

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.util.UUID

class BluetoothLeService : Service() {

    companion object {
        private val TAG: String = "fwhyn_test_" + BluetoothLeService::class.java.simpleName

        const val EXTRA_DATA = "EXTRA_DATA"
        val UUID_HEART_RATE_MEASUREMENT: UUID = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT)

        private const val SCAN_PERIOD: Long = 10000
    }

    private var bluetoothGatt: BluetoothGatt? = null

    // ----------------------------------------------------------------
    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "BLE service onBind")
        return LocalBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "BLE service onUnbind")
        close()
        return super.onUnbind(intent)
    }

    // ----------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bleScanner: BluetoothLeScanner? = null

    fun initialize(): Boolean {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bleScanner = bluetoothAdapter?.bluetoothLeScanner

        if (bluetoothAdapter == null || bleScanner == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    // ----------------------------------------------------------------
    private var scanning = false
    private var connecting = false
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingPermission")
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = device.name
            val deviceAddress = device.address

            Log.d(TAG, "BLE Found: $deviceName $deviceAddress")
            broadcastUpdate(BleServiceConstant.DEVICE_FOUND, BleData(device, null, null))

            if (deviceName == "PM102266" && !connecting) {
                bleScanner?.stopScanning(this)
                connect(device.address)
                connecting = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun scanDevice() {

        if (!scanning) {
            handler.postDelayed(
                {
                    bleScanner?.stopScanning(leScanCallback)
                    scanning = false
                },
                SCAN_PERIOD
            )

            bleScanner?.startScanning(leScanCallback)
            scanning = true
        }
    }

    @SuppressLint("MissingPermission")
    fun BluetoothLeScanner.startScanning(callback: ScanCallback) {
        Log.d(com.fwhyn.connectivity.helper.TAG, "BluetoothLeScanner startScanning invoked")
        broadcastUpdate(BleServiceConstant.SCANNING)
        this.startScan(callback)
    }

    @SuppressLint("MissingPermission")
    fun BluetoothLeScanner.stopScanning(callback: ScanCallback) {
        Log.d(com.fwhyn.connectivity.helper.TAG, "BluetoothLeScanner stopScanning invoked")
        broadcastUpdate(BleServiceConstant.SCAN_STOPPED)
        this.stopScan(callback)
    }

    // ----------------------------------------------------------------
    private val bluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.d(TAG, "onConnectionStateChange; status: $status; newState: $newState")

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                broadcastUpdate(BleServiceConstant.CONNECTED)

                // Attempts to discover services after successful connection.
                bluetoothGatt?.discoverServices()
            } else {
                // disconnected from the GATT Server
                broadcastUpdate(BleServiceConstant.DISCONNECTED)
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            Log.d(TAG, "onServicesDiscovered received: $status")

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(BleServiceConstant.SERVICES_DISCOVERED)

                val service: BluetoothGattService? =
                    gatt?.getService(UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455"))

                // Find the desired characteristic
                val characteristic = service?.getCharacteristic(
                    UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616")
                ).also {
                    Log.d(TAG, "ECG Service found")
                }

                // Read the characteristic value
                gatt?.run {
                    setCharacteristicNotification(characteristic, true)
                    readCharacteristic(characteristic)
                }.also {
                    Log.d(TAG, "Gatt found")
                }

            } else {
                // TODO service error
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            Log.d(TAG, "onCharacteristicChanged invoked")
            broadcastUpdate(BleServiceConstant.DATA_AVAILABLE, BleData(gatt.device, characteristic, value))

        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            Log.d(TAG, "onCharacteristicRead received: $status")

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(BleServiceConstant.DATA_AVAILABLE, BleData(gatt.device, characteristic, value))
            } else {
                // TODO service error
            }
        }
    }

    private fun broadcastUpdate(action: BleServiceConstant, bleData: BleData? = null) {
        val intent = Intent(action.name)

        bleData.let {
            intent.putExtra(EXTRA_DATA, bleData)
        }

        sendBroadcast(intent)
    }

    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE)
                Log.d(TAG, "Connecting to $address")
                return true
            } catch (exception: IllegalArgumentException) {
                Log.e(TAG, "Device not found with provided address. Unable to connect.")
                return false
            }
        } ?: run {
            Log.e(TAG, "BluetoothAdapter not initialized")
            return false
        }
    }

    fun getSupportedGattServices(): List<BluetoothGattService>? {
        return bluetoothGatt?.services
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.readCharacteristic(characteristic) ?: run {
            Log.d(TAG, "BluetoothGatt not initialized")
        }
    }

    @SuppressLint("MissingPermission")
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(characteristic, enabled)

            // This is specific to Heart Rate Measurement.
            if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
                val descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
                )
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        } ?: run {
            Log.d(TAG, "BluetoothGatt not initialized")
        }
    }

    // ----------------------------------------------------------------
    enum class BleServiceConstant(val value: String) {
        SCANNING("SCANNING"),
        SCAN_STOPPED("SCAN_STOPPED"),
        DEVICE_FOUND("DEVICE_FOUND"),
        CONNECTED("CONNECTED"),
        DISCONNECTED("DISCONNECTED"),
        SERVICES_DISCOVERED("SERVICES_DISCOVERED"),
        DATA_AVAILABLE("DATA_AVAILABLE"),
    }

    // ----------------------------------------------------------------
    inner class LocalBinder : Binder() {
        fun getService(): BluetoothLeService {
            return this@BluetoothLeService
        }
    }
}