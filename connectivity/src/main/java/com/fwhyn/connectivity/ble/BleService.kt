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
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.fwhyn.connectivity.helper.hexToByteArrayOrNull
import java.util.UUID

class BleService : Service() {

    companion object {
        private val TAG: String = "fwhyn_test_" + BleService::class.java.simpleName

        const val EXTRA_DATA = "EXTRA_DATA"

        private const val SCAN_PERIOD: Long = 30000 // msec

        private const val ANDESFIT_PM102266_SERVICE_UUID = "49535343-FE7D-4AE5-8FA9-9FAFD205E455"

        //        private const val ANDESFIT_PM102266_READ_UUID = "49535343-1E4D-4BD9-BA61-23C647249616"
        private const val ANDESFIT_PM102266_READ_UUID = "0000ff02-0000-1000-8000-00805f9b34fb"
        private const val ANDESFIT_PM102266_WRITE_UUID = "49535343-8841-43F4-A8D4-ECBE34729BB3"
        private const val CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    }

    private var characteristicMap: HashMap<String, BluetoothGattCharacteristic> = hashMapOf()

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
                },
                SCAN_PERIOD
            )

            bleScanner?.startScanning(leScanCallback)
        }
    }

    @SuppressLint("MissingPermission")
    fun BluetoothLeScanner.startScanning(callback: ScanCallback) {
        Log.d(TAG, "BluetoothLeScanner startScanning invoked")
        broadcastUpdate(BleServiceConstant.SCANNING)
        this.startScan(callback)
        scanning = true
    }

    @SuppressLint("MissingPermission")
    fun BluetoothLeScanner.stopScanning(callback: ScanCallback) {
        Log.d(TAG, "BluetoothLeScanner stopScanning invoked")
        broadcastUpdate(BleServiceConstant.SCAN_STOPPED)
        this.stopScan(callback)
        scanning = false
    }

    // ----------------------------------------------------------------
    private var sequence: AndesfitPM10Sequence = AndesfitPM10Sequence.UNINITIALIZED
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
                connecting = false
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            bluetoothGatt = gatt

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered: success")
                broadcastUpdate(BleServiceConstant.SERVICES_DISCOVERED)

                val service: BluetoothGattService? = gatt?.getService(UUID.fromString(ANDESFIT_PM102266_SERVICE_UUID))

                // Find the desired characteristic
                service?.getCharacteristic(UUID.fromString(ANDESFIT_PM102266_WRITE_UUID))?.let {
                    characteristicMap[ANDESFIT_PM102266_WRITE_UUID] = it
                }

                service?.getCharacteristic(UUID.fromString(ANDESFIT_PM102266_READ_UUID))?.let {
                    characteristicMap[ANDESFIT_PM102266_READ_UUID] = it
                }

                initDevice()
            } else {
                // TODO service error
                Log.d(TAG, "onServicesDiscovered: error")
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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicRead: success")
                broadcastUpdate(BleServiceConstant.DATA_AVAILABLE, BleData(gatt.device, characteristic, value))

                Log.d(TAG, "Sequence: ${sequence.name}")
                when (sequence) {
                    AndesfitPM10Sequence.SET_DEVICE_COMPLETED -> confirmSetDevice()
                    else -> {}
                }
            } else {
                // TODO service error
                Log.d(TAG, "onCharacteristicRead: error")
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicWrite: success")
                Log.d(TAG, "Sequence: ${sequence.name}")
                when (sequence) {
                    AndesfitPM10Sequence.SET_DEVICE -> setDeviceCompleted()
                    else -> {}
                }
            } else {
                // TODO service error
                Log.d(TAG, "onCharacteristicWrite: error")
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorWrite received: success")
                Log.d(TAG, "Sequence: ${sequence.name}")
                when (sequence) {
                    AndesfitPM10Sequence.INITIALIZED -> setDevice()
                    else -> {}
                }

            } else {
                // TODO service error
                Log.d(TAG, "onDescriptorWrite received: error")
            }
        }
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

    private fun initDevice() {
//        val characteristic = BluetoothGattCharacteristic(
//            UUID.fromString("your_characteristic_uuid"),
//            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
//            BluetoothGattCharacteristic.PERMISSION_READ
//        )

//        notifyCharacteristic(characteristicMap[ANDESFIT_PM102266_READ_UUID], true)
        val byteArray = if (true) {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }
        writeDescriptor(characteristicMap[ANDESFIT_PM102266_READ_UUID], byteArray)
        sequence = AndesfitPM10Sequence.INITIALIZED
    }

    private fun setDevice() {
        val byteArray = "83010101010102000000000000000000000000000000000000000000000000000000".hexToByteArrayOrNull()
        writeCharacteristic(characteristicMap[ANDESFIT_PM102266_WRITE_UUID], "8301".hexToByteArrayOrNull())
        sequence = AndesfitPM10Sequence.SET_DEVICE
    }

    private fun setDeviceCompleted() {
        readCharacteristic(characteristicMap[ANDESFIT_PM102266_READ_UUID])
        sequence = AndesfitPM10Sequence.SET_DEVICE_COMPLETED
    }

    private fun confirmSetDevice() {
        writeCharacteristic(characteristicMap[ANDESFIT_PM102266_WRITE_UUID], "FE".hexToByteArrayOrNull())
        sequence = AndesfitPM10Sequence.CONFIRMED_SET_DEVICE
    }

    @SuppressLint("MissingPermission")
    fun notifyCharacteristic(characteristic: BluetoothGattCharacteristic?, enable: Boolean) {
        if (bluetoothGatt != null && characteristic != null) {
            bluetoothGatt?.setCharacteristicNotification(characteristic, enable)

            val byteArray = if (enable) {
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            } else {
                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            }
            writeDescriptor(characteristic, byteArray)

        } else {
            Log.e(TAG, "notifyCharacteristic error")
        }
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        if (bluetoothGatt != null && characteristic != null) {
            bluetoothGatt?.readCharacteristic(characteristic)
        } else {
            Log.e(TAG, "readCharacteristic error")
        }
    }

    @SuppressLint("MissingPermission")
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic?, byteArray: ByteArray?) {
        if (bluetoothGatt != null && characteristic != null && byteArray != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bluetoothGatt?.writeCharacteristic(
                    characteristic,
                    byteArray,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            } else {
                characteristic.value = byteArray
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        } else {
            Log.e(TAG, "writeCharacteristic error")
        }
    }

    @SuppressLint("MissingPermission")
    fun writeDescriptor(characteristic: BluetoothGattCharacteristic?, byteArray: ByteArray?) {
        if (bluetoothGatt != null && characteristic != null && byteArray != null) {
            val descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bluetoothGatt?.writeDescriptor(descriptor, byteArray)
            } else {
                characteristic.value = byteArray
                bluetoothGatt?.writeDescriptor(descriptor)
            }
        } else {
            Log.e(TAG, "writeDescriptor error")
        }
    }

    private fun broadcastUpdate(action: BleServiceConstant, bleData: BleData? = null) {
        val intent = Intent(action.name)

        bleData.let {
            intent.putExtra(EXTRA_DATA, bleData)
        }

        sendBroadcast(intent)
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

    enum class AndesfitPM10Sequence {
        UNINITIALIZED,
        INITIALIZED,
        SET_DEVICE,
        SET_DEVICE_COMPLETED,
        CONFIRMED_SET_DEVICE,
        SET_DATE_TIME,
        SET_DATE_TIME_COMPLETED,
        CONFIRMED_SET_DATE_TIME,
    }

    // ----------------------------------------------------------------
    inner class LocalBinder : Binder() {
        fun getService(): BleService {
            return this@BleService
        }
    }
}