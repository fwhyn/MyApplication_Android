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
import android.bluetooth.BluetoothStatusCodes
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

        private const val ANDESFIT_PM102266_R_UUID = "49535343-1E4D-4BD9-BA61-23C647249616"
        private const val ANDESFIT_PM102266_RW_UUID = "49535343-6DAA-4D02-ABF6-19569ACA69FE"
        private const val ANDESFIT_PM102266_W_UUID = "49535343-8841-43F4-A8D4-ECBE34729BB3"

        private const val CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    }

    private var characteristicMap: HashMap<String, BluetoothGattCharacteristic> = hashMapOf()

    private var bluetoothGatt: BluetoothGatt? = null
        @SuppressLint("MissingPermission")
        set(value) {
            field = value
            field?.let {
                val connectionPriority = BluetoothGatt.CONNECTION_PRIORITY_HIGH
                it.requestConnectionPriority(connectionPriority)
            }
        }

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

            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
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

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered: success")
                broadcastUpdate(BleServiceConstant.SERVICES_DISCOVERED)

                val service: BluetoothGattService? = gatt?.getService(UUID.fromString(ANDESFIT_PM102266_SERVICE_UUID))

                // Find the desired characteristic
                service?.getCharacteristic(UUID.fromString(ANDESFIT_PM102266_W_UUID))?.let {
                    characteristicMap[ANDESFIT_PM102266_W_UUID] = it
                }

                service?.getCharacteristic(UUID.fromString(ANDESFIT_PM102266_R_UUID))?.let {
                    characteristicMap[ANDESFIT_PM102266_R_UUID] = it
                }

                service?.getCharacteristic(UUID.fromString(ANDESFIT_PM102266_RW_UUID))?.let {
                    characteristicMap[ANDESFIT_PM102266_RW_UUID] = it
                }

                initDevice()
            } else {
                // TODO service error
                Log.d(TAG, "onServicesDiscovered: error")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            characteristicChanged(gatt, characteristic, characteristic?.value)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            characteristicChanged(gatt, characteristic, value)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            characteristicRead(gatt, characteristic, characteristic?.value, status)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            characteristicRead(gatt, characteristic, value, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicWrite: success, Sequence: ${sequence.name}")

                when (sequence) {
                    AndesfitPM10Sequence.SET_DEVICE -> setDeviceCompleted()
                    AndesfitPM10Sequence.SET_DEVICE_COMPLETED -> confirmSetDevice()
                    else -> {}
                }
            } else {
                // TODO service error
                Log.d(TAG, "onCharacteristicWrite: error, Sequence: ${sequence.name}")
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            descriptorRead(gatt, descriptor, status, descriptor?.value)
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
            value: ByteArray
        ) {
            descriptorRead(gatt, descriptor, status, value)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorWrite: success, Sequence: ${sequence.name}")
                when (sequence) {
                    AndesfitPM10Sequence.INITIALIZED -> setDevice()
                    else -> {}
                }

            } else {
                // TODO service error
                Log.d(TAG, "onDescriptorWrite: error, Sequence: ${sequence.name}")
            }
        }
    }

    private fun characteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        value: ByteArray?
    ) {
        Log.d(TAG, "onCharacteristicChanged invoked")
        broadcastUpdate(BleServiceConstant.DATA_AVAILABLE, BleData(gatt?.device, characteristic, value))
    }

    private fun characteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        value: ByteArray?,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "onCharacteristicRead: success, Sequence: ${sequence.name}")
            broadcastUpdate(BleServiceConstant.DATA_AVAILABLE, BleData(gatt?.device, characteristic, value))

            when (sequence) {
                AndesfitPM10Sequence.SET_DEVICE_COMPLETED -> confirmSetDevice()
                else -> {}
            }
        } else {
            // TODO service error
            Log.d(TAG, "onCharacteristicRead: error, Sequence: ${sequence.name}")
        }
    }

    private fun descriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int,
        value: ByteArray?
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "onDescriptorRead: success, Sequence: ${sequence.name}")
            when (sequence) {
                else -> {}
            }

        } else {
            // TODO service error
            Log.d(TAG, "onDescriptorRead: error, Sequence: ${sequence.name}")
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
        sequence = AndesfitPM10Sequence.INITIALIZED
//        val characteristic = BluetoothGattCharacteristic(
//            UUID.fromString(ANDESFIT_PM102266_READ_UUID),
//            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
//            BluetoothGattCharacteristic.PERMISSION_READ
//        )
//        notifyCharacteristic(characteristic, true)

        notifyCharacteristic(characteristicMap[ANDESFIT_PM102266_R_UUID], true)

        setDevice()
    }

    private fun setDevice() {
        sequence = AndesfitPM10Sequence.SET_DEVICE

        val byteArray = "83010101010102000000000000000000000000000000000000000000000000000000".hexToByteArrayOrNull()
        writeCharacteristic(characteristicMap[ANDESFIT_PM102266_RW_UUID], byteArray)
    }

    private fun setDeviceCompleted() {
        sequence = AndesfitPM10Sequence.SET_DEVICE_COMPLETED

        readCharacteristic(characteristicMap[ANDESFIT_PM102266_RW_UUID])
    }

    private fun confirmSetDevice() {
        sequence = AndesfitPM10Sequence.CONFIRMED_SET_DEVICE

        writeCharacteristic(characteristicMap[ANDESFIT_PM102266_RW_UUID], "FE".hexToByteArrayOrNull())
    }

    @SuppressLint("MissingPermission")
    fun notifyCharacteristic(characteristic: BluetoothGattCharacteristic?, enable: Boolean) {
        val success: Boolean
        if (bluetoothGatt != null && characteristic != null) {
            success = bluetoothGatt?.setCharacteristicNotification(characteristic, enable) ?: false

//            val byteArray = if (enable) {
//                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//            } else {
//                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
//            }
//            writeDescriptor(characteristic, byteArray)
        } else {
            success = false
        }

        logCharacteristicProperties("notifyCharacteristic", success, characteristic)
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        val success = if (bluetoothGatt != null && characteristic != null) {
            bluetoothGatt?.readCharacteristic(characteristic) ?: false
        } else {
            false
        }

        logCharacteristicProperties("readCharacteristic", success, characteristic)
    }

    @SuppressLint("MissingPermission")
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic?, byteArray: ByteArray?) {
        val success = if (bluetoothGatt != null && characteristic != null && byteArray != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                BluetoothStatusCodes.SUCCESS == bluetoothGatt?.writeCharacteristic(
                    characteristic,
                    byteArray,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            } else {
                characteristic.value = byteArray
                bluetoothGatt?.writeCharacteristic(characteristic) ?: false
            }
        } else {
            false
        }

        logCharacteristicProperties("writeCharacteristic", success, characteristic)
    }

    @SuppressLint("MissingPermission")
    fun readDescriptor(characteristic: BluetoothGattCharacteristic?) {
        Log.d(TAG, "readDescriptor invoked")

        val success = if (bluetoothGatt != null && characteristic != null) {
            val descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID))
            bluetoothGatt?.readDescriptor(descriptor) ?: false
        } else {
            false
        }

        logCharacteristicProperties("readDescriptor", success, characteristic)
    }

    @SuppressLint("MissingPermission")
    fun writeDescriptor(characteristic: BluetoothGattCharacteristic?, byteArray: ByteArray?) {
        Log.d(TAG, "writeDescriptor invoked")

        val success = if (bluetoothGatt != null && characteristic != null && byteArray != null) {
            val descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                BluetoothStatusCodes.SUCCESS == bluetoothGatt?.writeDescriptor(descriptor, byteArray)
            } else {
                characteristic.value = byteArray
                bluetoothGatt?.writeDescriptor(descriptor) ?: false
            }
        } else {
            false
        }

        logCharacteristicProperties("writeDescriptor", success, characteristic)
    }

    private fun broadcastUpdate(action: BleServiceConstant, bleData: BleData? = null) {
        val intent = Intent(action.name)

        bleData.let {
            intent.putExtra(EXTRA_DATA, bleData)
        }

        sendBroadcast(intent)
    }

    private fun getCharacteristic(
        bluetoothGattService: BluetoothGattService,
        characteristicUuid: String
    ): BluetoothGattCharacteristic? {
        for (bluetoothGattCharacteristic in bluetoothGattService.characteristics) {
            if (bluetoothGattCharacteristic.uuid.toString().equals(characteristicUuid, true)) {
                return bluetoothGattCharacteristic
            }
        }

        throw RuntimeException("$TAG Characteristic not found: $characteristicUuid")
    }

    private fun getService(serviceUuid: String): BluetoothGattService {
        val gatt: BluetoothGatt = bluetoothGatt ?: throw RuntimeException("$TAG No device connected")

        for (bluetoothGattService in gatt.services) {
            if (bluetoothGattService.uuid.toString().equals(serviceUuid, true)) {
                return bluetoothGattService
            }
        }

        throw RuntimeException("$TAG Service not found")
    }

    private fun logCharacteristicProperties(
        message: String,
        isSuccess: Boolean,
        characteristic: BluetoothGattCharacteristic?
    ) {
        val characteristicProperties = characteristic?.properties

        val isReadEnabled = (characteristicProperties?.and(BluetoothGattCharacteristic.PROPERTY_READ)) != 0
        val isWriteEnabled = (characteristicProperties?.and(BluetoothGattCharacteristic.PROPERTY_WRITE)) != 0
        val isNotifyEnabled = (characteristicProperties?.and(BluetoothGattCharacteristic.PROPERTY_NOTIFY)) != 0

        if (isSuccess) {
            Log.d(
                TAG,
                "$message success: " +
                        "UUID: ${characteristic?.uuid}, " +
                        "R: $isReadEnabled, " +
                        "W: $isWriteEnabled, " +
                        "N:$isNotifyEnabled"
            )
        } else {
            Log.e(
                TAG,
                "$message error: " +
                        "UUID: ${characteristic?.uuid}, " +
                        "R: $isReadEnabled, " +
                        "W: $isWriteEnabled, " +
                        "N:$isNotifyEnabled"
            )
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