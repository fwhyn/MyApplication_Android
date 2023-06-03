package com.fwhyn.bluetooth.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.bluetooth.permission.PermissionManager

class BluetoothCheck(
    private val activity: Activity,
    activityResultCaller: ActivityResultCaller
) : PermissionManager(activityResultCaller) {

    private lateinit var enableBluetoothCallback: ActivityResultCallback<ActivityResult>

    private var launcher: ActivityResultLauncher<Intent> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            enableBluetoothCallback.onActivityResult(result)
        }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun bleCheck(bluetoothCheckCallback: BluetoothCheckCallback) {
        if (bleSupported()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val btPermission = Manifest.permission.BLUETOOTH_CONNECT

                checkPermissions(activity, arrayOf(btPermission), object :
                    PermissionCallback {
                    override fun onPermissionGranted() {
                        checkEnabledBluetooth(bluetoothCheckCallback) { result ->
                            setEnableBluetoothResult(result, bluetoothCheckCallback)
                        }
                    }

                    override fun onRequestRationale(permissions: List<String>) {
                        bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NEED_RATIONALE)
                    }

                    override fun onPermissionDenied(permissions: List<String>) {
                        requestPermission(btPermission) {
                            if (it) {
                                checkEnabledBluetooth(bluetoothCheckCallback) { result ->
                                    setEnableBluetoothResult(result, bluetoothCheckCallback)
                                }
                            } else {
                                bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NO_PERMISSION)
                            }
                        }
                    }
                })
            } else {
                checkEnabledBluetooth(bluetoothCheckCallback) { result ->
                    setEnableBluetoothResult(result, bluetoothCheckCallback)
                }
            }
        } else {
            bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NOT_SUPPORTED)
        }
    }

    private fun bleSupported(): Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun checkEnabledBluetooth(
        bluetoothCheckCallback: BluetoothCheckCallback,
        enableBluetoothCallback: ActivityResultCallback<ActivityResult>
    ) {
        this.enableBluetoothCallback = enableBluetoothCallback

        if (bluetoothEnabled()) {
            bluetoothCheckCallback.ableToScan()
        } else {
            requestEnableBluetooth()
        }
    }

    private fun bluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    private fun requestEnableBluetooth() {
        launcher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    private fun setEnableBluetoothResult(
        result: ActivityResult,
        bluetoothCheckCallback: BluetoothCheckCallback
    ) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            bluetoothCheckCallback.ableToScan()
        } else {
            bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.BT_OFF)
        }
    }
}