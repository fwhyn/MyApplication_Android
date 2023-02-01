package com.fwhyn.bluetooth.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.bluetooth.permission.PermissionCheck
import com.fwhyn.bluetooth.permission.PermissionMgr
import com.fwhyn.bluetooth.permission.PermissionRequest

// Singleton, activity from Application
class BtCheck(
    private val activity: Activity,
    private val permissionCheck: PermissionCheck,
    private val permissionRequest: PermissionRequest,
    private val btMgr: BtMgr
    ) {

    init {

    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    /**
     * Please register the launcher before activity created
     */
    fun registerLauncher(activity: ActivityResultCaller): BtLauncher{
        return BtLauncher(enableBtLauncher(activity), permissionLauncher(activity))
    }

    private fun enableBtLauncher(activity: ActivityResultCaller): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                btMgr.ableToScan()
            } else {
                btMgr.unableToScan()
            }
        }
    }

    private fun permissionLauncher(activity: ActivityResultCaller): ActivityResultLauncher<String> {
        return permissionRequest.registerLauncher(activity) {
            if (it) {
                btMgr.ableToScan()
            }
        }
    }

    fun bleCheck(launcher: BtLauncher) {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT

        if (!bleSupported()) {
            btMgr.unableToScan()
        } else {
            permissionCheck.permissionsCheck(arrayOf(btPermission), object : PermissionMgr {
                override fun onPermissionGranted() {
                    // Ensures Bluetooth is available on the device and it is enabled. If not,
                    // displays a dialog requesting user permission to enable Bluetooth.
                    if (!bluetoothEnabled()) {
                        launcher.enableBtLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                        // run permission and enable bluetooth
                    } else {
                        btMgr.ableToScan()
                    }
                }

                override fun onRequestRationale(permissions: Map<String, Boolean>) {
                    TODO("Not yet implemented")
                }

                override fun onPermissionDenied(permissions: Map<String, Boolean>) {
                    permissionRequest.requestPermission(btPermission, launcher.permissionLauncher)
                }
            })
        }
    }

    private fun bleSupported() : Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun bluetoothEnabled() : Boolean = bluetoothAdapter?.isEnabled == true
}