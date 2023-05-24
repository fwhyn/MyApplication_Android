package com.fwhyn.myapplication.util.other

import com.fwhyn.myapplication.util.other.queue.Doctor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NoRelationTest {
    private lateinit var noRelation: NoRelation
    private lateinit var doctors: List<Doctor>

    @Before
    fun init() {
        noRelation = NoRelation()

        val doctorA = Doctor(5)
        val doctorB = Doctor(4)
        val doctorC = Doctor(2)

        doctors = listOf(doctorA, doctorB, doctorC)
    }

    @Test(expected = StorageException::class)
    fun retrieveSectionShouldThrowOnInvalidFileName() {
        NoRelation().retrieveSection("invalid - file")
    }

    @Test
    fun getTimeTest() {
        // doctor > patient -> expected 0
        Assert.assertEquals(0, noRelation.getTime(doctors, doctors.size - 1))
    }
}