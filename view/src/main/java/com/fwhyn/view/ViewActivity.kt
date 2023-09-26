package com.fwhyn.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.view.databinding.ActivityResultBinding

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}