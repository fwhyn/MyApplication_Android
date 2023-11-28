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
import com.fwhyn.connectivity.permission.SuppressWarning
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class BluetoothCheck(
    private val componentActivity: ComponentActivity,
    private val bluetoothCheckCallback: BluetoothCheckCallback
) {

    companion object {
        const val BLUETOOTH_CHECK = 19248
    }

    private val permissionManager = PermissionManager(
        componentActivity,
        object : PermissionCheckCallback {
            override fun onPermissionGranted() {
                checkPhoneSensors()
            }

            override fun onRequestRationale(rationalePermissions: Array<String>) {
                bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NEED_RATIONALE)
            }

            override fun onPermissionDenied(deniedPermissions: Array<String>) {
                bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NO_PERMISSION)
            }
        })

    private val enableBluetoothLauncher: ActivityResultLauncher<Intent> =
        componentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setEnableBluetoothResult(result)
        }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (componentActivity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    @SuppressWarning
    fun bleCheck() {
        if (bleSupported()) {
            val permissionsToRequest = getNecessaryPermissions()

            if (permissionsToRequest.isNotEmpty()) {
                permissionManager.checkOrRequestPermissions(permissionsToRequest)
            } else {
                checkPhoneSensors()
            }
        } else {
            bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.NOT_SUPPORTED)
        }
    }

    private fun getNecessaryPermissions(): Array<String> {
        val permissions = ArrayList<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
            permissions.add(coarseLocationPermission)

            val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
            permissions.add(fineLocationPermission)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bluetoothScanPermission = Manifest.permission.BLUETOOTH_SCAN
            permissions.add(bluetoothScanPermission)

            val bluetoothAdvertisePermission = Manifest.permission.BLUETOOTH_ADVERTISE
            permissions.add(bluetoothAdvertisePermission)

            val bluetoothConnectPermission = Manifest.permission.BLUETOOTH_CONNECT
            permissions.add(bluetoothConnectPermission)

            val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
            permissions.remove(fineLocationPermission)
        }

        return permissions.toTypedArray()
    }

    private fun bleSupported(): Boolean {
        return componentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
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
            bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.BT_OFF)
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

        val client: SettingsClient = LocationServices.getSettingsClient(componentActivity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            bluetoothCheckCallback.ableToScan()
        }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(componentActivity, BLUETOOTH_CHECK)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.LOCATION_OFF)
                    }
                } else {
                    bluetoothCheckCallback.unableToScan(BluetoothCheckCallback.Reason.LOCATION_OFF)
                }
            }
    }
}