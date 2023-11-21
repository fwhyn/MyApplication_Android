package com.fwhyn.myapplication.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwhyn.myapplication.R
import com.fwhyn.myapplication.databinding.ActivityMainBinding
import com.fwhyn.myapplication.domain.helper.Results
import com.fwhyn.myapplication.domain.model.ModuleModel
import com.fwhyn.myapplication.ui.common.BaseActivityBinding
import com.fwhyn.myapplication.ui.helper.showToast
import com.fwhyn.myapplication.util.compose.TryComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        initView()
        observeData()
    }

    private fun initView() {
        viewBinding.buttonMain.setOnClickListener {
            // Snackbar.make(binding.root, R.string.test, Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(this, TryComposeActivity::class.java))
        }

        with(viewBinding.mainList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ModuleAdapter(
                viewModel.modules,
                clickListener = {
                    startActivity(Intent(this@MainActivity, it.cls))
                }
            )
        }
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

        viewBinding.mainList.adapter?.notifyDataSetChanged()
    }
}