package com.fwhyn.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fwhyn.bluetooth.permission.PermissionCheck

private const val SCAN_PERIOD = 10000L
private const val REQUEST_ENABLE_BT = 1
private const val REQUEST_BT_PERMISSION = 1

class BluetoothActivity : AppCompatActivity() {

    private lateinit var activity: Activity

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    // startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show()
                Looper.myLooper()?.let {  scanLeDevice(true, Handler(it)) }
            }

            RESULT_CANCELED -> {

            }

            else -> {

            }
        }
    }

    val permissionCheck = PermissionCheck(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        activity = this

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
            Looper.myLooper()?.let {  scanLeDevice(true, Handler(it)) }
        }

        runBle()
    }

    fun runBle() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!bleSupported()) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            runBluetooth()
        }
    }

    fun bleSupported() : Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    fun bluetoothEnabled() : Boolean = bluetoothAdapter?.isEnabled == true

    fun runBluetooth() {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (!bluetoothEnabled()) {
            val btConnect = Manifest.permission.BLUETOOTH_CONNECT
            val permissions = arrayOf(btConnect)

            // run permission and enable bluetooth
        } else {
            Looper.myLooper()?.let {  scanLeDevice(true, Handler(it)) }
        }
    }

    private var mScanning: Boolean = false

    private fun scanLeDevice(enable: Boolean, handler: Handler) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
        } else {
            when (enable) {
                true -> {
                    // Stops scanning after a pre-defined scan period.
                    handler.postDelayed({
                        mScanning = false
                        bluetoothAdapter?.stopLeScan(leScanCallback)
                    }, SCAN_PERIOD)
                    mScanning = true
                    bluetoothAdapter?.startLeScan(leScanCallback)
                }
                else -> {
                    mScanning = false
                    bluetoothAdapter?.stopLeScan(leScanCallback)
                }
            }
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        Log.d("fwhyn_test", "$rssi")
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_BT_PERMISSION = 1
    }
}