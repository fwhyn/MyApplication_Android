package com.fwhyn.bluetooth.permission

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class PermissionCheckTest {
    private lateinit var permissionCheck: PermissionCheck

    @Before
    fun init() {
        permissionCheck = Mockito.mock(
            PermissionCheck::class.java,
            Mockito.CALLS_REAL_METHODS
        )
    }

    @Test
    fun isOneOfPermissionsDeniedTest() {
        val privateMethod = permissionCheck.javaClass.getDeclaredMethod("isOneOfPermissionsDenied", String::class.java)
        privateMethod.isAccessible = true

        privateMethod.invoke(permissionCheck)

    }
}