package com.fwhyn.myapplication.rack

data class Rack(
    val id: Int,
    val w: Int,
    val l: Int,
    val h: Int,
    val total_floor: Int,
    val floor_a: Int = 0,
    val floor_b: Int = 0,
    val floor_c: Int = 0,
    val floor_d: Int = 0,
    val floor_e: Int = 0,
    val floor_f: Int = 0
    )