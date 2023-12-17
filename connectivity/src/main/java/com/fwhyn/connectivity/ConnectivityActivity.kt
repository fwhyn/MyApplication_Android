package com.fwhyn.connectivity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.ble.BleChecker
import com.fwhyn.connectivity.ble.BleCheckerCallback
import com.fwhyn.connectivity.bluetooth.BtChecker
import com.fwhyn.connectivity.bluetooth.BtCheckerCallback
import com.fwhyn.connectivity.bluetooth.BtManager
import com.fwhyn.connectivity.permission.PermissionManager
import com.fwhyn.connectivity.permission.PermissionManagerWarning

class ConnectivityActivity : AppCompatActivity() {

    //    private val bleManager = BleManager(this)
    @OptIn(PermissionManagerWarning::class)
    private val bleChecker = BleChecker(this, object : BleCheckerCallback {
        override fun ableToScan() {
            onBleAbleToScan()
        }

        override fun unableToScan(reason: BleCheckerCallback.Reason) {
            onBleUnableToScan(reason)
        }
    })

    private val btManager = BtManager(this)

    @OptIn(PermissionManagerWarning::class)
    private val btChecker = BtChecker(this, object : BtCheckerCallback {
        override fun ableToScan() {
            onBtAbleToScan()
        }

        override fun unableToScan(reason: BtCheckerCallback.Reason, permissions: Array<String>?) {
            onBtUnableToScan(reason, permissions)
        }
    })

    // ----------------------------------------------------------------
    @OptIn(PermissionManagerWarning::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
//            startActivity(Intent(this, DeviceScanActivity::class.java))
//            bleChecker.bleCheck()
            btChecker.btCheck()
        }
    }

    override fun onResume() {
        super.onResume()

//        bleManager.callWhenOnResume()
    }

    override fun onPause() {
        super.onPause()

//        bleManager.callWhenOnPause()
    }

    // ----------------------------------------------------------------
    private fun onBleAbleToScan() {
        Toast.makeText(this@ConnectivityActivity, "Scanning...", Toast.LENGTH_SHORT).show()

//        bleManager.scanDevice()
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

    private fun onBtAbleToScan() {
        Toast.makeText(this@ConnectivityActivity, "Able to scan", Toast.LENGTH_SHORT).show()

//        btManager.scanDevice()
    }

    private fun onBtUnableToScan(reason: BtCheckerCallback.Reason, permissions: Array<String>?) {
        when (reason) {
            BtCheckerCallback.Reason.NEED_RATIONALE -> PermissionManager.openSetting(this@ConnectivityActivity)
            BtCheckerCallback.Reason.NO_PERMISSION -> {
                showToast("$reason ${permissions?.size}")
            }

            BtCheckerCallback.Reason.BT_OFF -> showToast(reason.toString())
            BtCheckerCallback.Reason.LOCATION_OFF -> showToast(reason.toString())
            BtCheckerCallback.Reason.NOT_SUPPORTED -> showToast(reason.toString())
        }
    }

    private fun showToast(string: String) {
        Toast.makeText(this@ConnectivityActivity, string, Toast.LENGTH_SHORT).show()
    }
}