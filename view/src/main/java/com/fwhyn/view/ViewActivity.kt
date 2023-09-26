package com.fwhyn.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.view.databinding.ActivityViewBinding
import com.fwhyn.view.databinding.UserActivity

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonResult.setOnClickListener {
            startActivity(Intent(this@ViewActivity, UserActivity::class.java))
        }
    }
}