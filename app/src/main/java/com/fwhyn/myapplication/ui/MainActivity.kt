package com.fwhyn.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwhyn.myapplication.databinding.ActivityMainBinding
import com.fwhyn.myapplication.ui.common.recyclerview.CustomAdapter
import com.fwhyn.myapplication.util.compose.TryComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMain.setOnClickListener {
            // Snackbar.make(binding.root, R.string.test, Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(this, TryComposeActivity::class.java))
        }

        with(binding.mainList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CustomAdapter(
                listOf(),
                clickListener = {
                    startActivity(Intent(this@MainActivity, it.cls))
                }
            )
        }

        init()
    }

    private fun init() {

    }

    private fun observeData() {
        mainActivityViewModel.observableModules.observe(this) {

        }
    }
}