package com.fwhyn.myapplication.ui.common

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseActivityBinding<VB : ViewBinding> : BaseActivity() {

    lateinit var viewBinding: VB

    abstract fun onBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = onBinding()
        setContentView(viewBinding.root)
    }
}