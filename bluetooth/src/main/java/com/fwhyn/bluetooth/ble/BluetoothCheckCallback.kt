package com.fwhyn.bluetooth.ble

interface BluetoothCheckCallback {

    fun ableToScan()
    fun unableToScan(reason: Reason)

    // --------------------------------
    enum class Reason {
        NEED_RATIONALE, NO_PERMISSION, BT_OFF, NOT_SUPPORTED
    }
}