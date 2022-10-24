package com.fwhyn.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class MyActivityTest {
    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testGreeting() {
        val greeting = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getString(R.string.greeting)

        composeTestRule.onNodeWithText(greeting).assertIsDisplayed()
    }
}