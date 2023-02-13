package com.fwhyn.bluetooth

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.bluetooth.ble.BtCheck
import com.fwhyn.bluetooth.ble.BtLauncher
import com.fwhyn.bluetooth.ble.BtMgr
import com.fwhyn.bluetooth.permission.PermissionCheck
import com.fwhyn.bluetooth.permission.PermissionRequest

class BluetoothActivity : AppCompatActivity() {

    private lateinit var launcher: BtLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        val btCheck = BtCheck(this, PermissionCheck(this), PermissionRequest())
        launcher = btCheck.registerLauncher(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
            btCheck.bleCheck(launcher, object : BtMgr {
                override fun ableToScan() {
                    Toast.makeText(this@BluetoothActivity, "Ok", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun unableToScan(reason: BtMgr.Reason) {
                    Toast.makeText(this@BluetoothActivity, reason.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }
}