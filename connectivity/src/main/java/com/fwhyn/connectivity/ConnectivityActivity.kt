package com.fwhyn.connectivity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.ble.BleChecker
import com.fwhyn.connectivity.ble.BleCheckerCallback
import com.fwhyn.connectivity.ble.BleManager
import com.fwhyn.connectivity.permission.PermissionManager
import com.fwhyn.connectivity.permission.PermissionManagerWarning

class ConnectivityActivity : AppCompatActivity() {

    private lateinit var bleManager: BleManager

    private val bleChecker = BleChecker(this, object : BleCheckerCallback {
        override fun ableToScan() {
            onBleAbleToScan()
        }

        override fun unableToScan(reason: BleCheckerCallback.Reason) {
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
//            startActivity(Intent(this, DeviceScanActivity::class.java))
            bleChecker.bleCheck()
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

    private fun onBleUnableToScan(reason: BleCheckerCallback.Reason) {
        when (reason) {
            BleCheckerCallback.Reason.NEED_RATIONALE -> PermissionManager.openSetting(this@ConnectivityActivity)
            BleCheckerCallback.Reason.NO_PERMISSION -> showToast(reason.toString())
            BleCheckerCallback.Reason.BT_OFF -> showToast(reason.toString())
            BleCheckerCallback.Reason.LOCATION_OFF -> showToast(reason.toString())
            BleCheckerCallback.Reason.NOT_SUPPORTED -> showToast(reason.toString())
        }
    }

    private fun showToast(string: String) {
        Toast.makeText(this@ConnectivityActivity, string, Toast.LENGTH_SHORT).show()
    }
}