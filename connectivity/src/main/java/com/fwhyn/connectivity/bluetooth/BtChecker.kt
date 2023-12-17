package com.fwhyn.connectivity.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fwhyn.connectivity.helper.getBtAdapterOrNull
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
class BtChecker(
    private val activity: ComponentActivity,
    private val callback: BtCheckerCallback
) : DefaultLifecycleObserver {

    companion object {
        private val TAG: String = "fwhyn_test_" + BtChecker::class.java.simpleName

        const val BLUETOOTH_CHECK = 19248
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    private val permissionManager = PermissionManager.getInstance(
        activity,
        object : PermissionCheckCallback {
            override fun onPermissionGranted() {
                checkPhoneSensors()
            }

            override fun onRequestRationale(rationalePermissions: Array<String>) {
                callback.unableToScan(BtCheckerCallback.Reason.NEED_RATIONALE, rationalePermissions)
            }

            override fun onPermissionDenied(deniedPermissions: Array<String>) {
                callback.unableToScan(BtCheckerCallback.Reason.NO_PERMISSION, deniedPermissions)
            }
        })

    private var bluetoothAdapter: BluetoothAdapter? = null

    // ----------------------------------------------------------------
    override fun onCreate(owner: LifecycleOwner) {
        // Log.d(TAG, "onCreate invoked")
        init()
    }

    // ----------------------------------------------------------------
    private fun init() {
        bluetoothAdapter = activity.getBtAdapterOrNull()
    }

    fun btCheck() {
        if (btSupported()) {
            val permissionsToRequest = getNecessaryPermissions()

            if (permissionsToRequest.isNotEmpty()) {
                permissionManager.checkOrRequestPermissions(permissionsToRequest)
            } else {
                checkPhoneSensors()
            }
        } else {
            callback.unableToScan(BtCheckerCallback.Reason.NOT_SUPPORTED, null)
        }
    }

    private fun btSupported(): Boolean {
        return bluetoothAdapter != null
    }

    private fun getNecessaryPermissions(): Array<String> {
        val permissions = hashSetOf<String>()


        permissions.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
                add(Manifest.permission.ACCESS_FINE_LOCATION)
            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_ADMIN)
                add(Manifest.permission.BLUETOOTH_SCAN)
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        return permissions.toTypedArray()
    }

    private fun checkPhoneSensors() {
        if (bluetoothEnabled()) {
            createLocationRequest()
        } else {
            requestEnableBluetooth()
        }
    }

    private fun bluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    private val enableBluetoothLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setEnableBluetoothResult(result)
        }

    private fun requestEnableBluetooth() {
        enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    private fun setEnableBluetoothResult(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            createLocationRequest()
        } else {
            callback.unableToScan(BtCheckerCallback.Reason.BT_OFF, null)
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
                        callback.unableToScan(BtCheckerCallback.Reason.LOCATION_OFF, null)
                    }
                } else {
                    callback.unableToScan(BtCheckerCallback.Reason.LOCATION_OFF, null)
                }
            }
    }
}