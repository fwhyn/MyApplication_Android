package com.fwhyn.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwhyn.myapplication.R
import com.fwhyn.myapplication.databinding.ActivityMainBinding
import com.fwhyn.myapplication.domain.helper.Results
import com.fwhyn.myapplication.domain.model.ModuleModel
import com.fwhyn.myapplication.ui.common.adapter.CustomAdapter
import com.fwhyn.myapplication.ui.helper.showToast
import com.fwhyn.myapplication.util.compose.TryComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

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
                viewModel.modules,
                clickListener = {
                    startActivity(Intent(this@MainActivity, it.cls))
                }
            )
        }

        init()
    }

    private fun init() {
        observeData()
    }

    private fun observeData() {
        viewModel.observableModules.observe(this) {
            when (it) {
                is Results.Success -> updateModuleList(it.data)
                is Results.Loading -> showToast(getString(R.string.loading) + " " + it.progress)
                is Results.Failure -> showToast(R.string.error)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateModuleList(modules: List<ModuleModel>) {
        viewModel.modules.run {
            clear()
            addAll(modules)
        }

        binding.mainList.adapter?.notifyDataSetChanged()
    }
}