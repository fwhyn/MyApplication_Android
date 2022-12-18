package com.fwhyn.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fwhyn.viewbinding.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}