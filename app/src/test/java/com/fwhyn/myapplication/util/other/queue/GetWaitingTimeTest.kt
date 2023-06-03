package com.fwhyn.myapplication.util.other.queue

import com.fwhyn.myapplication.util.other.NoRelation
import com.fwhyn.myapplication.util.other.StorageException
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetWaitingTimeTest {
    private lateinit var noRelation: GetWaitingTime

    private lateinit var doctors0: List<Doctor>
    private lateinit var doctors1: List<Doctor>
    private lateinit var doctors2: List<Doctor>
    private lateinit var doctors3: List<Doctor>
    private lateinit var doctors4: List<Doctor>
    private lateinit var doctors2i: List<Doctor>

    @Before
    fun before() {
        noRelation = GetWaitingTime()

        val doctorA = Doctor(3)
        val doctorB = Doctor(4)
        val doctorC = Doctor(2)
        val doctorD = Doctor(13)

        val doctorE = Doctor(4, 2)

        doctors0 = listOf()
        doctors1 = listOf(doctorA)
        doctors2 = listOf(doctorA, doctorB)
        doctors3 = listOf(doctorA, doctorB, doctorC)
        doctors4 = listOf(doctorA, doctorB, doctorC, doctorD)

        doctors2i = listOf(doctorA, doctorE)
    }

    @Test(expected = StorageException::class)
    fun retrieveSectionShouldThrowOnInvalidFileName() {
        NoRelation().retrieveSection("invalid - file")
    }

    // doctor A: 0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 46
    // doctor B: 0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 38, 42, 46
    // doctor C: 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32
    // doctor D: 0, 13, 26, 39, 52
    // doctor E: 2, 6, 10, 14, 18, 22, 26, 30, 34

    @Test
    fun getWaitingTimeTest4Doctors() {
        // 0, 0, 0, 0, 2, 3, 4, 4, 6, 6, 8, 8, 9, 10, 12, 12, 12, 13, 14, 15, 16
        Assert.assertEquals(0, noRelation.getWaitingTimeOrNull(doctors4, 4))
        Assert.assertEquals(2, noRelation.getWaitingTimeOrNull(doctors4, 5))
        Assert.assertEquals(3, noRelation.getWaitingTimeOrNull(doctors4, 6))
        Assert.assertEquals(4, noRelation.getWaitingTimeOrNull(doctors4, 7))
        Assert.assertEquals(4, noRelation.getWaitingTimeOrNull(doctors4, 8))
        Assert.assertEquals(6, noRelation.getWaitingTimeOrNull(doctors4, 9))
        Assert.assertEquals(6, noRelation.getWaitingTimeOrNull(doctors4, 10))
        Assert.assertEquals(8, noRelation.getWaitingTimeOrNull(doctors4, 11))
        Assert.assertEquals(10, noRelation.getWaitingTimeOrNull(doctors4, 14))
        Assert.assertEquals(12, noRelation.getWaitingTimeOrNull(doctors4, 17))
        Assert.assertEquals(13, noRelation.getWaitingTimeOrNull(doctors4, 18))
        Assert.assertEquals(14, noRelation.getWaitingTimeOrNull(doctors4, 19))
    }

    @Test
    fun getWaitingTimeTest3Doctors() {
        // doctor > patient -> expected 0
        Assert.assertEquals(0, noRelation.getWaitingTimeOrNull(doctors3, 1))
        Assert.assertEquals(0, noRelation.getWaitingTimeOrNull(doctors3, 2))

        // doctor = patient -> expected 0
        Assert.assertEquals(0, noRelation.getWaitingTimeOrNull(doctors3, 3))

        // doctor + 1 = patient
        Assert.assertEquals(2, noRelation.getWaitingTimeOrNull(doctors3, 4))

        // doctor > patient
        // 0, 0, 0, 2, 3, 4, 4, 6, 6, 8, 9, 12
        Assert.assertEquals(3, noRelation.getWaitingTimeOrNull(doctors3, 5))
        Assert.assertEquals(6, noRelation.getWaitingTimeOrNull(doctors3, 8))
        Assert.assertEquals(6, noRelation.getWaitingTimeOrNull(doctors3, 9))
    }

    @Test
    fun getWaitingTimeTest2Doctors() {
        // 11 patients: 0, 0, 3, 4, 6, 8, 9, 12, 12, 15, 16
        Assert.assertEquals(16, noRelation.getWaitingTimeOrNull(doctors2, 11))

        // 15 patients: 0, 0, 3, 4, 6, 8, 9, 12, 12, 15, 16, 18, 20, 21, 24
        Assert.assertEquals(24, noRelation.getWaitingTimeOrNull(doctors2, 15))

        // 26 patients: 0, 0, 3, 4, 6, 8, 9, 12, 12, 15, 16, 18, 20, 21, 24, 24, 27, 28, 30, 32,
        // 33, 36, 36, 38, 39, 42
    }

    @Test
    fun getWaitingTimeTest1Doctor() {
        // 0, 3, 6, 9, 12, 15, 18, 21
        Assert.assertEquals(0, noRelation.getWaitingTimeOrNull(doctors1, 1))
        Assert.assertEquals(15, noRelation.getWaitingTimeOrNull(doctors1, 6))
        Assert.assertEquals(18, noRelation.getWaitingTimeOrNull(doctors1, 7))
    }

    @Test
    fun getWaitingTimeTest2DoctorsWithInitTime() {
        // 0, 2, 3, 6, 6, 9, 10, 12, 14, 15, 18, 18, 21, 22, 24, 26, 27, 30, 30, 33, 34
        Assert.assertEquals(2, noRelation.getWaitingTimeOrNull(doctors2i, 2))
        Assert.assertEquals(6, noRelation.getWaitingTimeOrNull(doctors2i, 5))
        Assert.assertEquals(9, noRelation.getWaitingTimeOrNull(doctors2i, 6))
        Assert.assertEquals(18, noRelation.getWaitingTimeOrNull(doctors2i, 11))
        Assert.assertEquals(18, noRelation.getWaitingTimeOrNull(doctors2i, 12))
    }

    @Test
    fun getWaitingTimeTestNoPatientReturnsNull() {
        Assert.assertNull(noRelation.getWaitingTimeOrNull(doctors1, 0))
        Assert.assertNull(noRelation.getWaitingTimeOrNull(doctors2, 0))
    }

    @Test
    fun getWaitingTimeTestNoDoctorReturnsNull() {
        Assert.assertNull(noRelation.getWaitingTimeOrNull(doctors0, 0))
        Assert.assertNull(noRelation.getWaitingTimeOrNull(doctors0, 1))
    }
}