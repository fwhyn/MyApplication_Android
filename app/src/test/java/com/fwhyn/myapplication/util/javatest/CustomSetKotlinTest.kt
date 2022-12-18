package com.fwhyn.myapplication.util.javatest

import com.fwhyn.myapplication.util.javatest.CustomSetKotlin
import org.junit.Assert
import org.junit.Test

internal class CustomSetKotlinTest {

    @Test
    fun add() {

    }

    @Test
    fun isSame() {
        Assert.assertTrue(CustomSetKotlin().isSame(2, 2))
        Assert.assertTrue(CustomSetKotlin().isSame("Test", "Test"))
        Assert.assertTrue(CustomSetKotlin().isSame())
    }
}