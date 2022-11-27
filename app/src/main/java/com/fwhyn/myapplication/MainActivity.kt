package com.fwhyn.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_main).setOnClickListener {
//            startActivity(Intent(this, UserActivity::class.java))
//            startActivity(Intent(this, CocktailsGameActivity::class.java))
//            startActivity(Intent(this, WishlistSplashActivity::class.java))
        }
    }
}