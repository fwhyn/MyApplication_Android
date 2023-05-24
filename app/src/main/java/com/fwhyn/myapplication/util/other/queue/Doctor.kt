package com.fwhyn.myapplication.util.other.queue

data class Doctor(
    val consultationTimeMinute: Int,
    val initialTime: Int = 0, // value > 0 if already seeing patient
    var timeStep: Int = initialTime - consultationTimeMinute
)
