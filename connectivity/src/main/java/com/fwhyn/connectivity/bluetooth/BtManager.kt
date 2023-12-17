package com.fwhyn.connectivity.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fwhyn.connectivity.helper.getBtAdapterOrNull
import com.fwhyn.connectivity.helper.getParcelable


class BtManager(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    companion object {
        private val TAG: String = "fwhyn_test_" + BtManager::class.java.simpleName

        private const val DISCOVERABLE_TIME = 15 // sec
        private const val SCAN_PERIOD = 15000L // msec
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    private val btReceiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d(TAG, "Scanning...")
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d(TAG, "Scan stopped")
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelable(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)

                    Log.d(TAG, "Device Found: ${device?.name}")
                }
            }
        }
    }

    // ----------------------------------------------------------------
    override fun onCreate(owner: LifecycleOwner) {
        init()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unregisterBtReceiver()
    }

    // ----------------------------------------------------------------
    private fun init() {
        Log.d(TAG, "init")

        bluetoothAdapter = activity.getBtAdapterOrNull()
        registerBtReceiver()
    }

    private fun registerBtReceiver() {
        activity.registerReceiver(
            btReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
        )
    }

    private fun unregisterBtReceiver() {
        activity.unregisterReceiver(btReceiver)
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

    private var scanning = false

    @SuppressLint("MissingPermission")
    fun scan() {
//        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
//            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME)
//        }
//
//        activity.startActivity(intent)

        val handler = Handler(Looper.getMainLooper())
        if (!scanning) {
            handler.postDelayed(
                { stopScan() },
                SCAN_PERIOD
            )

            bluetoothAdapter.scan()
            scanning = true
        }
    }

    fun stopScan() {
        if (scanning) {
            bluetoothAdapter.stopScan()
            scanning = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun BluetoothAdapter?.scan() {
        this?.startDiscovery() ?: Log.e(TAG, "Bluetooth Adapter not found")
    }

    @SuppressLint("MissingPermission")
    private fun BluetoothAdapter?.stopScan() {
        this?.cancelDiscovery() ?: Log.e(TAG, "Bluetooth Adapter not found")
    }
}