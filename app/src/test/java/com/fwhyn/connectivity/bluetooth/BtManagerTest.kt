package com.fwhyn.connectivity.bluetooth

import com.fwhyn.connectivity.helper.hexToByteArrayOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class BtManagerTest {
    @Test
    fun randomTest() {
        val binaryString = "10010000"
        val binaryString2 = "0"

        // Convert binary string to BigInteger
        val bigInteger = BigInteger(binaryString, 2)
        val bigInteger2 = BigInteger(binaryString2, 2)

        // Convert BigInteger to byte array
        val byte = bigInteger.toByte()
        val byte2 = bigInteger2.toByte()

        val bytes = bigInteger.toByteArray()
        val byteArray = byteArrayOf(byte, byte2)

        assertTrue(true)
    }

    @Test
    fun stringToHex() {
        val x = "83".toInt(16)
        val y = x.toByte()

        val z = "831".hexToByteArrayOrNull()

        assertEquals(131, z)
    }
}