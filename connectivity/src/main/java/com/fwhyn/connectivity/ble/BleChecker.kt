package com.fwhyn.connectivity.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.connectivity.permission.PermissionCheckCallback
import com.fwhyn.connectivity.permission.PermissionManager
import com.fwhyn.connectivity.permission.PermissionManagerWarning
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

@PermissionManagerWarning
class BleChecker(
    private val activity: ComponentActivity,
    private val callback: BleCheckerCallback
) {

    companion object {
        const val BLUETOOTH_CHECK = 19248
    }

    private val permissionManager = PermissionManager.getInstance(
        activity,
        object : PermissionCheckCallback {
            override fun onPermissionGranted() {
                checkPhoneSensors()
            }

            override fun onRequestRationale(rationalePermissions: Array<String>) {
                callback.unableToScan(BleCheckerCallback.Reason.NEED_RATIONALE)
            }

            override fun onPermissionDenied(deniedPermissions: Array<String>) {
                callback.unableToScan(BleCheckerCallback.Reason.NO_PERMISSION)
            }
        })

    private val enableBluetoothLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setEnableBluetoothResult(result)
        }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun bleCheck() {
        if (bleSupported()) {
            val permissionsToRequest = getNecessaryPermissions()

            if (permissionsToRequest.isNotEmpty()) {
                permissionManager.checkOrRequestPermissions(permissionsToRequest)
            } else {
                checkPhoneSensors()
            }
        } else {
            callback.unableToScan(BleCheckerCallback.Reason.NOT_SUPPORTED)
        }
    }

    private fun getNecessaryPermissions(): Array<String> {
        val permissions = hashSetOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.run {
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
                add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.run {
                add(Manifest.permission.BLUETOOTH_ADMIN)
                add(Manifest.permission.BLUETOOTH_ADVERTISE)
                add(Manifest.permission.BLUETOOTH_CONNECT)
                add(Manifest.permission.BLUETOOTH_SCAN)
                remove(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        return permissions.toTypedArray()
    }

    private fun bleSupported(): Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun checkPhoneSensors() {
        if (bluetoothEnabled()) {
            createLocationRequest()
        } else {
            requestEnableBluetooth()
        }
    }

    private fun bluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    private fun requestEnableBluetooth() {
        enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    private fun setEnableBluetoothResult(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            createLocationRequest()
        } else {
            callback.unableToScan(BleCheckerCallback.Reason.BT_OFF)
        }
    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            callback.ableToScan()
        }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(activity, BLUETOOTH_CHECK)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        callback.unableToScan(BleCheckerCallback.Reason.LOCATION_OFF)
                    }
                } else {
                    callback.unableToScan(BleCheckerCallback.Reason.LOCATION_OFF)
                }
            }
    }
}