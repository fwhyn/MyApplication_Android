package com.fwhyn.myapplication.databindingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fwhyn.myapplication.databinding.ActivityUserBinding
import com.google.android.material.snackbar.Snackbar

class UserActivity : AppCompatActivity(), ClickHandler {
    private lateinit var parentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        parentView = binding.root
        setContentView(parentView)

        val userTest = User("Test", "User")
        binding.user = userTest
        binding.presenter = Presenter()
    }

    override fun onClick(view: View) {
        Snackbar.make(parentView, "tset", Snackbar.LENGTH_SHORT)
            .setAction("close", null)
            .show()
    }
}