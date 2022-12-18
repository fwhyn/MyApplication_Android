package com.fwhyn.myapplication.util.rack

data class PrinterBase(
    val name: String,
    val w: Int,
    val l: Int,
    val h: Int,
    val weight: Int,
    val stackable: Boolean,
    // which parameter that has smallest difference length with rack width
    // temporary set with printer width
    var rackAlign: Int = w,
    // will be aligned with rack length
    // temporary set with printer length
    var rackLengthFill: Int = l
    ) {
    val bottomArea: Int
        get() = w * l
}