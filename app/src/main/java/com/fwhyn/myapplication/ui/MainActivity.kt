package com.fwhyn.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwhyn.myapplication.R
import com.fwhyn.myapplication.databinding.ActivityMainBinding
import com.fwhyn.myapplication.ui.common.recyclerview.CustomAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set view
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMain.setOnClickListener {
            Snackbar.make(binding.root, R.string.test, Snackbar.LENGTH_SHORT).show()
        }

        with(binding.mainList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CustomAdapter(
                mainActivityViewModel.getModules(),
                clickListener = {
                    startActivity(Intent(this@MainActivity, it.cls))
                }
            )
        }
    }
}