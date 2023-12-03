package com.fwhyn.connectivity.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import com.fwhyn.connectivity.ble.BluetoothLeService.Companion.ACTION_GATT_CONNECTED
import com.fwhyn.connectivity.ble.BluetoothLeService.Companion.ACTION_GATT_DISCONNECTED

class BleManager(private val activity: ComponentActivity) {

    companion object {
        private val TAG: String = "fwhyn_test_" + BleManager::class.java.simpleName

        private const val SCAN_PERIOD: Long = 10000
    }

    private val bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private var bleService: BluetoothLeService? = null
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            bleService = (service as BluetoothLeService.LocalBinder).getService()
            bleService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth")
                    activity.finish()
                }

                // perform device connection
//                bluetooth.connect(deviceAddress)
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bleService = null
        }
    }

    init {
        val gattServiceIntent = Intent(activity, BluetoothLeService::class.java)
        activity.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.action) {
//                BluetoothLeService.ACTION_GATT_CONNECTED -> {
//                    connected = true
//                    updateConnectionState(R.string.connected)
//                }
//                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
//                    connected = false
//                    updateConnectionState(R.string.disconnected)
//                }
//                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
//                    // Show all the supported services and characteristics on the user interface.
//                    displayGattServices(bleService?.getSupportedGattServices())
//                }
//            }
        }
    }

    // Demonstrates how to iterate through the supported GATT
    // Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the
    // ExpandableListView on the UI.
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
//        if (gattServices == null) return
//        var uuid: String?
//        val unknownServiceString: String = resources.getString(R.string.unknown_service)
//        val unknownCharaString: String = resources.getString(R.string.unknown_characteristic)
//        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
//        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> =
//            mutableListOf()
//        mGattCharacteristics = mutableListOf()
//
//        // Loops through available GATT Services.
//        gattServices.forEach { gattService ->
//            val currentServiceData = HashMap<String, String>()
//            uuid = gattService.uuid.toString()
//            currentServiceData[LIST_NAME] = SampleGattAttributes.lookup(uuid, unknownServiceString)
//            currentServiceData[LIST_UUID] = uuid
//            gattServiceData += currentServiceData
//
//            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
//            val gattCharacteristics = gattService.characteristics
//            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()
//
//            // Loops through available Characteristics.
//            gattCharacteristics.forEach { gattCharacteristic ->
//                charas += gattCharacteristic
//                val currentCharaData: HashMap<String, String> = hashMapOf()
//                uuid = gattCharacteristic.uuid.toString()
//                currentCharaData[LIST_NAME] = SampleGattAttributes.lookup(uuid, unknownCharaString)
//                currentCharaData[LIST_UUID] = uuid
//                gattCharacteristicGroupData += currentCharaData
//            }
//            mGattCharacteristics += charas
//            gattCharacteristicData += gattCharacteristicGroupData
//        }
    }

    fun callWhenOnResume() {
        activity.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
//        if (bleService != null) {
//            val result = bleService?.connect(deviceAddress)
//            Log.d(TAG, "Connect request result = $result")
//        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(ACTION_GATT_CONNECTED)
            addAction(ACTION_GATT_DISCONNECTED)
        }
    }

    fun callWhenOnPause() {
        activity.unregisterReceiver(gattUpdateReceiver)
    }

    // ----------------------------------------------------------------
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())

    private val leDeviceList = arrayListOf<ScanResult>()
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(TAG, "BLEW Found: ${result.device.name}")
            leDeviceList.add(result)
        }
    }

    @SuppressLint("MissingPermission")
    fun scanDevice() {
        val bleScanner = bluetoothAdapter.bluetoothLeScanner

        if (!scanning) {
            handler.postDelayed(
                {
                    bleScanner.stopScan(leScanCallback)
                    scanning = false
                    Log.d(TAG, "Scan stopped. Found Items: ${leDeviceList.size}")
                },
                SCAN_PERIOD
            )

            bleScanner.startScan(leScanCallback)
            scanning = true
            Log.d(TAG, "Scanning...")
        }
    }
}