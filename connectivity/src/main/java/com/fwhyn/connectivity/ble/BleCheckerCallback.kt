package com.fwhyn.connectivity.ble

import java.io.Serializable

interface BleCheckerCallback {

    fun ableToScan()
    fun unableToScan(reason: Reason)

    // --------------------------------
    enum class Reason : Serializable {
        NEED_RATIONALE, NO_PERMISSION, BT_OFF, LOCATION_OFF, NOT_SUPPORTED
    }
}