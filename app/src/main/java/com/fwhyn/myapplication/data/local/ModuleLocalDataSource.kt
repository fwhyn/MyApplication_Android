package com.fwhyn.myapplication.data.local

import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.databinding.UserActivity
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.viewbinding.ResultActivity
import com.fwhyn.wishlist.app.WishlistSplashActivity
import com.fwhyn.daggerhiltgreek.DaggerHiltActivity

class ModuleLocalDataSource {
    val moduleList = listOf(
        Module("WishList", WishlistSplashActivity::class.java),
        Module("Cocktail", CocktailsGameActivity::class.java),
        Module("View Binding", ResultActivity::class.java),
        Module("Data Binding", UserActivity::class.java),
        Module("Dagger Hilt Greek", DaggerHiltActivity::class.java),
    )
}