package com.fwhyn.bluetooth.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.bluetooth.permission.PermissionCheck
import com.fwhyn.bluetooth.permission.PermissionMgr
import com.fwhyn.bluetooth.permission.PermissionRequest

class BtCheck(
    private val activity: Activity,
    private val permissionCheck: PermissionCheck,
    private val permissionRequest: PermissionRequest,
) {
    private lateinit var launcher: BtLauncher
    private lateinit var btMgr: BtMgr

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    /**
     * Please register the launcher before activity created
     */
    fun registerLauncher(activityResultCaller: ActivityResultCaller): BtLauncher {
        return BtLauncher(
            enableBtLauncher(activityResultCaller),
            permissionLauncher(activityResultCaller)
        )
    }

    private fun enableBtLauncher(activityResultCaller: ActivityResultCaller): ActivityResultLauncher<Intent> {
        return activityResultCaller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                btMgr.ableToScan()
            } else {
                btMgr.unableToScan(BtMgr.Reason.BT_OFF)
            }
        }
    }

    private fun permissionLauncher(activityResultCaller: ActivityResultCaller): ActivityResultLauncher<String> {
        return permissionRequest.registerLauncher(activityResultCaller) {
            if (it) {
                runBt()
            } else {
                btMgr.unableToScan(BtMgr.Reason.NO_PERMISSION)
            }
        }
    }

    fun bleCheck(launcher: BtLauncher, btMgr: BtMgr) {
        this.launcher = launcher
        this.btMgr = btMgr

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (bleSupported()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val btPermission = Manifest.permission.BLUETOOTH_CONNECT
                permissionCheck.permissionsCheck(arrayOf(btPermission), object : PermissionMgr {
                    override fun onPermissionGranted() {
                        runBt()
                    }

                    override fun onRequestRationale(permissions: List<String>) {
                        btMgr.unableToScan(BtMgr.Reason.NEED_RATIONALE)
                    }

                    override fun onPermissionDenied(permissions: List<String>) {
                        permissionRequest.requestPermission(
                            btPermission,
                            launcher.permissionLauncher
                        )
                    }
                })
            } else {
                btMgr.ableToScan()
            }
        } else {
            btMgr.unableToScan(BtMgr.Reason.NOT_SUPPORTED)
        }
    }

    private fun bleSupported(): Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun runBt() {
        if (bluetoothEnabled()) {
            btMgr.ableToScan()
        } else {
            launcher.enableBtLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    private fun bluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true
}