package com.fwhyn.myapplication.ui

import androidx.lifecycle.ViewModel
import com.fwhyn.myapplication.data.ModuleRepository
import com.fwhyn.myapplication.domain.model.Module
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val moduleRepository: ModuleRepository) : ViewModel() {
    fun getModules(): List<Module> {
        return moduleRepository.getModules()
    }
}