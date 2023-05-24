package com.fwhyn.myapplication.util.other

import com.fwhyn.myapplication.util.other.queue.Doctor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NoRelationTest {
    private lateinit var noRelation: NoRelation

    @Before
    fun init() {
        noRelation = NoRelation()
    }

    @Test(expected = StorageException::class)
    fun retrieveSectionShouldThrowOnInvalidFileName() {
        NoRelation().retrieveSection("invalid - file")
    }

    @Test
    fun getTimeTest() {
        // doctor > patient -> expected 0
        Assert.assertEquals(0, noRelation.getTime(getDoctors1(), 1))
        Assert.assertEquals(0, noRelation.getTime(getDoctors1(), 2))

        // doctor = patient -> expected 0
        Assert.assertEquals(0, noRelation.getTime(getDoctors1(), 3))

        // doctor + 1 = patient
        Assert.assertEquals(2, noRelation.getTime(getDoctors1(), 4))

        // doctor > patient
        Assert.assertEquals(4, noRelation.getTime(getDoctors1(), 5))
    }

    @Test
    fun getTimeTest2() {
        // doctor > patient -> expected 0
        Assert.assertEquals(20, noRelation.getTime(getDoctors2(), 11))
    }

    private fun getDoctors1(): List<Doctor> {
        val doctorA = Doctor(5)
        val doctorB = Doctor(2)
        val doctorC = Doctor(4)

        return listOf(doctorA, doctorB, doctorC)
    }

    private fun getDoctors2(): List<Doctor> {
        val doctorA = Doctor(3)
        val doctorB = Doctor(4)

        return listOf(doctorA, doctorB)
    }
}