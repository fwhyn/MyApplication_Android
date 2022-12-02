package com.fwhyn.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwhyn.myapplication.databinding.ActivityMainBinding
import com.fwhyn.myapplication.ui.common.recyclerview.CustomAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMain.setOnClickListener {
            Snackbar.make(binding.root, R.string.test, Snackbar.LENGTH_SHORT).show()
        }

        with(binding.mainList) {
            adapter = CustomAdapter(Util.stringList)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}