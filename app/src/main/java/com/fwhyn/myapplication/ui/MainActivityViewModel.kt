package com.fwhyn.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.myapplication.domain.helper.Results
import com.fwhyn.myapplication.domain.model.ModuleModel
import com.fwhyn.myapplication.domain.usecase.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val getModuleUseCaseModel: BaseUseCase<Unit, List<ModuleModel>>) :
    ViewModel() {

    val modules = arrayListOf<ModuleModel>()

    private val _observableModules = MutableLiveData<Results<List<ModuleModel>, Exception>>()
    val observableModules: LiveData<Results<List<ModuleModel>, Exception>> = _observableModules

    init {
        getModules()
    }

    private fun getModules() {
        getModuleUseCaseModel
            .setResultNotifier {
                _observableModules.value = it
            }
            .execute(Unit, viewModelScope)
    }
}