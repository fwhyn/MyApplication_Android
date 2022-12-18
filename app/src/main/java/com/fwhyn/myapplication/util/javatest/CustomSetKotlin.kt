package com.fwhyn.myapplication.util.javatest

class CustomSetKotlin {

    private var objectKotlin = arrayOf<Any?>()
    fun customAdd(value: Any?) {
        objectKotlin[0] = value
    }

    fun isSame(a: Int, b: Int): Boolean {
        return (a == b)
    }

    fun isSame(a: String, b: String): Boolean {
        return (a == b)
    }

    fun isSame(): Boolean {
        val a = "Test"
        val b = a

        return b.equals(a)
    }

    fun test() {
        val a = arrayListOf<String> ("a", "b")
        a.add("b")

        val b = listOf<String> ("a", "b")

        val c = mutableListOf<String> ("a", "b")
        c.add("asgd")
    }
}