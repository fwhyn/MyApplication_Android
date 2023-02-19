package com.fwhyn.myapplication.util

import org.junit.Assert.*
import org.junit.Test

class UtilTest {
    @Test
    fun valueInRange() {
        val a = 3.1
        val b = 2.2
        val c = 9.3
        val d = a in b..c

        assertTrue(d)
    }

    @Test
    fun findValueInRange() {
        val a = 3.1
        val b = 2.2
        val mid = 9.3
        val d = 10.9
        val e = 11.6
        val x = 11.0

        val origArray = arrayOf(a, b, mid, d, e).sortedArray()

        var lower: Double
        var upper: Double
        // set default value if x is not in range
        var deviation = mid - a
        val size = origArray.size - 2

        for (i in 0..size) {
            lower = origArray[i]
            upper = origArray[i+1]

            if (x in lower..upper) {
                deviation = upper - lower
                break
            }
        }

        assertTrue(deviation > 0)
    }
}