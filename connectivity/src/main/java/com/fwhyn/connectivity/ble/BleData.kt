package com.fwhyn.connectivity.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class BleData(
    val device: BluetoothDevice?,
    val characteristic: BluetoothGattCharacteristic?,
    val value: ByteArray?,
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    constructor(parcel: Parcel, androidTiramisuOrAbove: Boolean = false) : this(
        parcel.readParcelable(BluetoothDevice::class.java.classLoader, BluetoothDevice::class.java),
        parcel.readParcelable(
            BluetoothGattCharacteristic::class.java.classLoader,
            BluetoothGattCharacteristic::class.java
        ),
        parcel.createByteArray()
    )

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(BluetoothDevice::class.java.classLoader),
        parcel.readParcelable(BluetoothGattCharacteristic::class.java.classLoader),
        parcel.createByteArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleData

        if (characteristic != other.characteristic) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = characteristic.hashCode()
        result = 31 * result + value.contentHashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(device, flags)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            parcel.writeParcelable(characteristic, flags)
        }
        parcel.writeByteArray(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BleData> {
        override fun createFromParcel(parcel: Parcel): BleData {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                BleData(parcel, true)
            } else {
                BleData(parcel)
            }

        }

        override fun newArray(size: Int): Array<BleData?> {
            return arrayOfNulls(size)
        }
    }
}