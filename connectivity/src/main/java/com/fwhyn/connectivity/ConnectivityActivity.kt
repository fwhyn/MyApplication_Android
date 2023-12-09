package com.fwhyn.connectivity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.ble.BleManager
import com.fwhyn.connectivity.ble.BluetoothCheck
import com.fwhyn.connectivity.ble.BluetoothCheckCallback
import com.fwhyn.connectivity.permission.PermissionManager
import com.fwhyn.connectivity.permission.PermissionManagerWarning

class ConnectivityActivity : AppCompatActivity() {

    private lateinit var bleManager: BleManager

    private val bluetoothCheck = BluetoothCheck(this, object : BluetoothCheckCallback {
        override fun ableToScan() {
            onBleAbleToScan()
        }

        override fun unableToScan(reason: BluetoothCheckCallback.Reason) {
            onBleUnableToScan(reason)
        }
    })

    // ----------------------------------------------------------------
    @OptIn(PermissionManagerWarning::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        bleManager = BleManager(this@ConnectivityActivity)

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
            bluetoothCheck.bleCheck()
        }
    }

    override fun onResume() {
        super.onResume()

        bleManager.callWhenOnResume()
    }

    override fun onPause() {
        super.onPause()

        bleManager.callWhenOnPause()
    }

    // ----------------------------------------------------------------
    private fun onBleAbleToScan() {
        Toast.makeText(this@ConnectivityActivity, "Scanning...", Toast.LENGTH_SHORT).show()

        bleManager.scanDevice()
    }

    private fun onBleUnableToScan(reason: BluetoothCheckCallback.Reason) {
        when (reason) {
            BluetoothCheckCallback.Reason.NEED_RATIONALE -> PermissionManager.openSetting(this@ConnectivityActivity)
            BluetoothCheckCallback.Reason.NO_PERMISSION -> showToast(reason.toString())
            BluetoothCheckCallback.Reason.BT_OFF -> showToast(reason.toString())
            BluetoothCheckCallback.Reason.LOCATION_OFF -> showToast(reason.toString())
            BluetoothCheckCallback.Reason.NOT_SUPPORTED -> showToast(reason.toString())
        }
    }

    private fun showToast(string: String) {
        Toast.makeText(this@ConnectivityActivity, string, Toast.LENGTH_SHORT).show()
    }
}