package com.fwhyn.bluetooth.ble

import com.fwhyn.bluetooth.permission.PermissionMgr

class BtPermission : PermissionMgr{
    override fun onPermissionGranted(permissions: String) {

    }

    override fun onRequestRationale(permission: String) {
        TODO("Not yet implemented")
    }

    override fun onPermissionDenied(permission: String) {
        TODO("Not yet implemented")
    }
}