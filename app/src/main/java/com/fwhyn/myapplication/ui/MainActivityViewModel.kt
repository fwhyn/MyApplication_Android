package com.fwhyn.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.myapplication.domain.helper.Results
import com.fwhyn.myapplication.domain.model.Module
import com.fwhyn.myapplication.domain.usecase.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val getModuleUseCase: BaseUseCase<Unit, List<Module>>) :
    ViewModel() {

    private val _observableModules = MutableLiveData<Results<List<Module>, Exception>>()
    val observableModules: LiveData<Results<List<Module>, Exception>> = _observableModules

    init {
        getModules()
    }

    private fun getModules() {
        getModuleUseCase
            .setResultNotifier {
                _observableModules.value = it
            }
            .execute(Unit, viewModelScope)
    }
}