package com.fwhyn.myapplication.databindingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fwhyn.myapplication.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userTest = User("Test", "User")

        binding.user = userTest

//        binding.buttonTest.setOnClickListener {
//            userTest.firstName = "aaa"
//        }
    }
}