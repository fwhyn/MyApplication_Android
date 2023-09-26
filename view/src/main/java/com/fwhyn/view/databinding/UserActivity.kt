package com.fwhyn.view.databinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class UserActivity : AppCompatActivity(), ClickHandler {
    private lateinit var parentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding view
        val binding = ActivityUserBinding.inflate(layoutInflater)
        parentView = binding.root
        setContentView(parentView)

        // Specify the current activity as the lifecycle owner.
        // to bind livedata
        binding.lifecycleOwner = this

        // binding class
        binding.user = User("Test", "User")
        binding.presenter = Presenter()
        binding.viewmodel = ScheduleViewModel()
    }

    override fun onClick(view: View) {
        Snackbar.make(parentView, "tset", Snackbar.LENGTH_SHORT)
            .setAction("close", null)
            .show()
    }
}