package com.fwhyn.connectivity.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.permission.PermissionCheckCallback
import com.fwhyn.connectivity.permission.PermissionManager

class BluetoothCheck(
    private val activity: Activity,
    activityResultCaller: ActivityResultCaller,
    private val bluetoothCheckCallback: BluetoothCheckCallback
) : PermissionCheckCallback {

    private val permissionManager = PermissionManager(activity, activityResultCaller, this, getNecessaryPermissions())

    private val enableBluetoothLauncher: ActivityResultLauncher<Intent> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setEnableBluetoothResult(result, bluetoothCheckCallback)
        }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun bleCheck() {
        if (bleSupported()) {
            val permissionsToRequest = getNecessaryPermissions()

            if (permissionsToRequest.isNotEmpty()) {
                permissionManager.checkOrRequestPermissions()
            } else {
                checkEnabledBluetooth(bluetoothCheckCallback)
            }
        } else {
            bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NOT_SUPPORTED)
        }
    }

    // --------------------------------
    override fun onPermissionGranted() {
        checkEnabledBluetooth(bluetoothCheckCallback)
    }

    override fun onRequestRationale(rationalePermissions: Array<String>) {
        bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NEED_RATIONALE)
    }

    override fun onPermissionDenied(deniedPermissions: Array<String>) {
        bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NO_PERMISSION)
    }

    // --------------------------------
    private fun getNecessaryPermissions(): Array<String> {
        val permissions = ArrayList<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
            val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION

            permissions.add(coarseLocationPermission)
            permissions.add(fineLocationPermission)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bluetoothPermission = Manifest.permission.BLUETOOTH_CONNECT

            permissions.add(bluetoothPermission)
        }

        return permissions.toTypedArray()
    }

    private fun bleSupported(): Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun checkEnabledBluetooth(bluetoothCheckCallback: BluetoothCheckCallback) {
        if (bluetoothEnabled()) {
            bluetoothCheckCallback.ableToScan()
        } else {
            requestEnableBluetooth()
        }
    }

    private fun bluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    private fun requestEnableBluetooth() {
        enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
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