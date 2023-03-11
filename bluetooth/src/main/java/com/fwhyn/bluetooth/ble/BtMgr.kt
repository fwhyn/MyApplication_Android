package com.fwhyn.bluetooth.ble

interface BtMgr {
    fun ableToScan()
    fun unableToScan(reason: Reason)

    // --------------------------------
    enum class Reason {
        NEED_RATIONALE, NO_PERMISSION, BT_OFF, NOT_SUPPORTED
    }
}
