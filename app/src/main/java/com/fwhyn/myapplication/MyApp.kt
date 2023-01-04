package com.fwhyn.myapplication

import android.app.Application
import com.fwhyn.wishlist.app.wishlistAppModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

@HiltAndroidApp
open class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    // start koin
    private fun startKoin() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                // declare Android context
                androidContext(this@MyApp)
                // declare used modules
                modules(
                    wishlistAppModule, //wishlist module
                )
            }
        }
    }
}