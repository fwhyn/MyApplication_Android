package com.fwhyn.connectivity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.ble.BleScan
import com.fwhyn.connectivity.ble.BluetoothCheck
import com.fwhyn.connectivity.ble.BluetoothCheckCallback

class ConnectivityActivity : AppCompatActivity() {
    private val bluetoothCheck = BluetoothCheck(this, object : BluetoothCheckCallback {
        override fun ableToScan() {
            // Toast.makeText(this@ConnectivityActivity, "Ok", Toast.LENGTH_SHORT).show()
            BleScan(this@ConnectivityActivity).scanLeDevice(true)
        }

        override fun unableToScan(reason: BluetoothCheckCallback.Reason) {
            Toast.makeText(this@ConnectivityActivity, reason.toString(), Toast.LENGTH_SHORT).show()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
            bluetoothCheck.bleCheck()
        }
    }
}