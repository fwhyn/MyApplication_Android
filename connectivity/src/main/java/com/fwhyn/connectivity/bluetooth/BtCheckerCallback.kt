package com.fwhyn.connectivity.bluetooth

import java.io.Serializable

interface BtCheckerCallback {

    fun ableToScan()
    fun unableToScan(reason: Reason, permissions: Array<String>?)

    // --------------------------------
    enum class Reason : Serializable {
        NEED_RATIONALE, NO_PERMISSION, BT_OFF, LOCATION_OFF, NOT_SUPPORTED
    }
}