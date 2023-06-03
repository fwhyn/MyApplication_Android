package com.fwhyn.bluetooth

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.bluetooth.ble.BluetoothCheck
import com.fwhyn.bluetooth.ble.BluetoothCheckCallback

class BluetoothActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val bluetoothCheck = BluetoothCheck(this, this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        findViewById<TextView>(R.id.hello_textview).setOnClickListener {
            bluetoothCheck.bleCheck(object : BluetoothCheckCallback {
                override fun ableToScan() {
                    Toast.makeText(this@BluetoothActivity, "Ok", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun unableToScan(reason: BluetoothCheckCallback.Reason) {
                    Toast.makeText(this@BluetoothActivity, reason.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }
}