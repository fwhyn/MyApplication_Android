package com.fwhyn.myapplication.data.local

import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.connectivity.ConnectivityActivity
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.mysoothe.MySootheActivity
import com.fwhyn.view.ViewActivity
import com.fwhyn.wishlist.app.WishlistSplashActivity
import fwhyn.corp.analytics.AnalyticsActivity

class ModuleLocalDataSource {
    val moduleList = listOf(
        Module("View", ViewActivity::class.java),
        Module("Connectivity", ConnectivityActivity::class.java),
        Module("Analytics", AnalyticsActivity::class.java),
        Module("Cocktail", CocktailsGameActivity::class.java),
        Module("WishList", WishlistSplashActivity::class.java),
        Module("My Soothe", MySootheActivity::class.java),
    )
}