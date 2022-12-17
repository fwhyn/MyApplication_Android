package com.fwhyn.myapplication.di

import com.fwhyn.myapplication.data.di.DataModule
import com.fwhyn.myapplication.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}