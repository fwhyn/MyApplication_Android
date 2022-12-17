package com.fwhyn.myapplication.ui.feature

import android.content.Context
import android.content.Intent
import com.fwhyn.myapplication.ui.common.NewIntent
import com.fwhyn.wishlist.app.WishlistSplashActivity

class WishListBridge(private val context: Context) : NewIntent {

    override fun createIntent(): Intent {
        return Intent(context, WishlistSplashActivity::class.java)
    }
}