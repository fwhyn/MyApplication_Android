package com.fwhyn.myapplication.javatest

class TestKotlin {
    private var objectKotlin = arrayOfNulls<Any>(4)
    fun add(value: Any?) {
        objectKotlin[0] = value
    }
}