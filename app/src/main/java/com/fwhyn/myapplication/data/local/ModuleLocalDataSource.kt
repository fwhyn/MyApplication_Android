package com.fwhyn.myapplication.data.local

import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.connectivity.ConnectivityActivity
import com.fwhyn.myapplication.domain.model.ModuleModel
import com.fwhyn.mysoothe.MySootheActivity
import com.fwhyn.view.ViewActivity
import com.fwhyn.wishlist.app.WishlistSplashActivity
import fwhyn.corp.analytics.AnalyticsActivity

class ModuleLocalDataSource {
    val moduleModelLists = listOf(
        ModuleModel("View", ViewActivity::class.java),
        ModuleModel("Connectivity", ConnectivityActivity::class.java),
        ModuleModel("Analytics", AnalyticsActivity::class.java),
        ModuleModel("Cocktail", CocktailsGameActivity::class.java),
        ModuleModel("WishList", WishlistSplashActivity::class.java),
        ModuleModel("My Soothe", MySootheActivity::class.java),
    )
}