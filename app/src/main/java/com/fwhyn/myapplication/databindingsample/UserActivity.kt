package com.fwhyn.myapplication.databindingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.fwhyn.myapplication.R
import com.fwhyn.myapplication.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.user = User("Test", "User")
    }
}