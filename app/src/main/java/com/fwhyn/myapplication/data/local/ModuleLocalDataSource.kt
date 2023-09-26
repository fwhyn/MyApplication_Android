package com.fwhyn.myapplication.data.local

import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.connectivity.ConnectivityActivity
import com.fwhyn.databinding.UserActivity
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.mysoothe.MySootheActivity
import com.fwhyn.viewbinding.ResultActivity
import com.fwhyn.wishlist.app.WishlistSplashActivity
import fwhyn.corp.googleanalyticstest.GoogleAnalyticsActivity

class ModuleLocalDataSource {
    val moduleList = listOf(
        Module("View Binding", ResultActivity::class.java),
        Module("Data Binding", UserActivity::class.java),
        Module("Connectivity", ConnectivityActivity::class.java),
        Module("Analytics", GoogleAnalyticsActivity::class.java),
        Module("Cocktail", CocktailsGameActivity::class.java),
        Module("WishList", WishlistSplashActivity::class.java),
        Module("My Soothe", MySootheActivity::class.java),
    )
}