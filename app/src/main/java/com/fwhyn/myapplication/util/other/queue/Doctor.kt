package com.fwhyn.myapplication.util.other.queue

data class Doctor(
    val consultationTimeMinute: Int,

    // positive if already seeing patient
    // negative if late seeing patent while other doctors have started
    val initialTime: Int = 0,

    var timeStep: Int = 0
)
