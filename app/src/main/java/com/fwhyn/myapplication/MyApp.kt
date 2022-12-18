package com.fwhyn.myapplication

import android.app.Application
import com.fwhyn.myapplication.di.AppComponent
import com.fwhyn.myapplication.di.DaggerAppComponent
import com.fwhyn.wishlist.app.wishlistAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

open class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.create()
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