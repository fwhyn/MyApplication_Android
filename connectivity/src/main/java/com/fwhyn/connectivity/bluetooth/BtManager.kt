package com.fwhyn.connectivity.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fwhyn.connectivity.helper.getBtAdapterOrNull
import com.fwhyn.connectivity.helper.getParcelable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


class BtManager(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    private val handler = Handler(Looper.getMainLooper())
    private var mmSocket: BluetoothSocket? = null
    private var state = State.IDLE

    companion object {
        private val TAG: String = "fwhyn_test_" + BtManager::class.java.simpleName

        // Defines several constants used when transmitting messages between the service and the UI.
        private const val MESSAGE_READ: Int = 0
        private const val MESSAGE_WRITE: Int = 1
        private const val MESSAGE_TOAST: Int = 2

        private const val DISCOVERABLE_TIME = 15 // sec
        private const val SCAN_PERIOD = 15000L // msec
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    private val btReceiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d(TAG, "Scanning...")
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d(TAG, "Scan stopped")
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelable(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)

                    Log.d(TAG, "Device Found: ${device?.name}")
                    if (device?.name == "PM102266") {
                        connect(device)
                    }
                }
            }
        }
    }

    // ----------------------------------------------------------------
    override fun onCreate(owner: LifecycleOwner) {
        init()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unregisterBtReceiver()
    }

    // ----------------------------------------------------------------
    private fun init() {
        Log.d(TAG, "init")

        bluetoothAdapter = activity.getBtAdapterOrNull()
        registerBtReceiver()
    }

    private fun registerBtReceiver() {
        activity.registerReceiver(
            btReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
        )
    }

    private fun unregisterBtReceiver() {
        activity.unregisterReceiver(btReceiver)
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevicesOrNull(deviceName: String? = null): Array<BluetoothDevice>? {
        var pairedDevices: HashSet<BluetoothDevice>? = null
        val tempPairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        deviceName?.let {
            tempPairedDevices?.let {
                pairedDevices = hashSetOf()
                it.forEach { device ->
                    if (deviceName == device.name) {
                        pairedDevices!!.add(device)
                    }
                }
            }
        } ?: {
            pairedDevices = tempPairedDevices?.toHashSet()
        }

        return pairedDevices?.toTypedArray()
    }

    @SuppressLint("MissingPermission")
    fun scan() {
        if (state != State.SCANNING) {
            handler.postDelayed(
                { stopScan() },
                SCAN_PERIOD
            )

            bluetoothAdapter.scan()
        }
    }

    fun stopScan() {
        if (state == State.SCANNING) {
            bluetoothAdapter.stopScan()
        }
    }

    @SuppressLint("MissingPermission")
    private fun BluetoothAdapter?.scan() {
        this?.startDiscovery() ?: Log.e(TAG, "Bluetooth Adapter not found")
        state = State.SCANNING
    }

    @SuppressLint("MissingPermission")
    private fun BluetoothAdapter?.stopScan() {
        this?.cancelDiscovery() ?: Log.e(TAG, "Bluetooth Adapter not found")
        state = State.IDLE
    }

    fun connect(device: BluetoothDevice) {
        if (state != State.CONNECTING) {
            disconnect()
            ConnectThread(device).start()
            Log.d(TAG, "ConnectThread")
        }
    }

    fun getData(socket: BluetoothSocket) {

    }

    fun disconnect() {
        try {
            // Close the BluetoothSocket
            mmSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // ----------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        init {
            val uuid = device.uuids?.toString()
            uuid?.let {
                mmSocket =
                    device.createRfcommSocketToServiceRecord(UUID.fromString("9535343-1E4D-4BD9-BA61-23C647249616"))
                Log.d(TAG, "setting socket")
            }
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            stopScan()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                Log.d(TAG, "device connected")
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                ConnectedThread(socket).start()
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ,
                    numBytes,
                    -1,
                    mmBuffer
                )
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE,
                -1,
                -1,
                bytes
            )
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    // ----------------------------------------------------------------
    enum class State() {
        SCANNING,
        IDLE,
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
    }
}