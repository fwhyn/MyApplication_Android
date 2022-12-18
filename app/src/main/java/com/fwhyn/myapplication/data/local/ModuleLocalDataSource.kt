package com.fwhyn.myapplication.data.local

import com.fwhyn.cocktails.CocktailsGameActivity
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.wishlist.app.WishlistSplashActivity

class ModuleLocalDataSource {
    val moduleList = listOf(
        Module("WishList", WishlistSplashActivity::class.java),
        Module("Cocktail", CocktailsGameActivity::class.java)
    )
}