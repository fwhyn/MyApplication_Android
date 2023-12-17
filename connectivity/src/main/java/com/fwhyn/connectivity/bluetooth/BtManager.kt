package com.fwhyn.connectivity.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fwhyn.connectivity.helper.getBtAdapterOrNull


class BtManager(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    companion object {
        private val TAG: String = "fwhyn_test_" + BtManager::class.java.simpleName
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    // ----------------------------------------------------------------
    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate invoked")
        init()
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume invoked")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause invoked")
    }

    // ----------------------------------------------------------------
    private fun init() {
        bluetoothAdapter = activity.getBtAdapterOrNull()
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevicesOrNull(deviceName: String? = null): Array<BluetoothDevice>? {
        var pairedDevices: HashSet<BluetoothDevice>? = null
        val tempPairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        deviceName?.let {
            tempPairedDevices?.let {
                pairedDevices = hashSetOf()
                it.forEach { device ->
                    if (deviceName == device.name) {
                        pairedDevices!!.add(device)
                    }
                }
            }
        } ?: {
            pairedDevices = tempPairedDevices?.toHashSet()
        }

        return pairedDevices?.toTypedArray()
    }
}