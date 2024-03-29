package com.fwhyn.connectivity.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattService
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fwhyn.connectivity.ble.BleService.BleServiceConstant.CONNECTED
import com.fwhyn.connectivity.ble.BleService.BleServiceConstant.DATA_AVAILABLE
import com.fwhyn.connectivity.ble.BleService.BleServiceConstant.DISCONNECTED
import com.fwhyn.connectivity.ble.BleService.BleServiceConstant.SERVICES_DISCOVERED


class BleManager(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    // TODO pindah ble checker ke ble manager
    companion object {
        private val TAG: String = "fwhyn_test_" + BleManager::class.java.simpleName
    }

    private var bleService: BleService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "BLE service connected")

            bleService = (service as BleService.LocalBinder).getService()
            bleService?.let {
                if (!it.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth")
                    activity.finish()
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.d(TAG, "BLE service disconnected")
            bleService = null
        }
    }

    init {
        activity.lifecycle.addObserver(this)

    }

    // ----------------------------------------------------------------
    override fun onCreate(owner: LifecycleOwner) {
        bindBleService()
    }

    override fun onResume(owner: LifecycleOwner) {
        registerReceiver()
    }

    override fun onPause(owner: LifecycleOwner) {
        unregisterReceiver()
    }

    // ----------------------------------------------------------------
    private fun bindBleService() {
        val gattServiceIntent = Intent(activity, BleService::class.java)
        activity.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "bindBleService invoked")
    }

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                CONNECTED.value -> {
                    // TODO implementation
                }

                DISCONNECTED.value -> {
                    // TODO implementation
                }

                SERVICES_DISCOVERED.value -> {
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(bleService?.getSupportedGattServices())
                }
            }
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter(), Context.RECEIVER_NOT_EXPORTED)

        } else {
            activity.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(CONNECTED.value)
            addAction(DISCONNECTED.value)
            addAction(SERVICES_DISCOVERED.value)
            addAction(DATA_AVAILABLE.value)
        }
    }

    private fun unregisterReceiver() {
        activity.unregisterReceiver(gattUpdateReceiver)
    }

    fun scanDevice() {
        bleService?.scanDevice() ?: run {
            Log.e(TAG, "BLE service is null")
        }
    }

    fun connectToDevice(deviceAddress: String) {
        bleService?.let {
            val result = it.connect(deviceAddress)
            Log.d(TAG, "Connect request result = $result")
        } ?: run {
            Log.e(TAG, "BLE service is null")
        }
    }
}