package com.fwhyn.myapplication.data.local

import com.fwhyn.bluetooth.BluetoothActivity
import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.databinding.UserActivity
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.viewbinding.ResultActivity
import com.fwhyn.wishlist.app.WishlistSplashActivity
import fwhyn.corp.googleanalyticstest.GoogleAnalyticsActivity

class ModuleLocalDataSource {
    val moduleList = listOf(
        Module("WishList", WishlistSplashActivity::class.java),
        Module("Cocktail", CocktailsGameActivity::class.java),
        Module("View Binding", ResultActivity::class.java),
        Module("Data Binding", UserActivity::class.java),
        Module("Google Analytics", GoogleAnalyticsActivity::class.java),
        Module("Bluetooth", BluetoothActivity::class.java),
    )
}