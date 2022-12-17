package com.fwhyn.myapplication.data

import com.fwhyn.myapplication.data.local.ModuleLocalDataSource
import com.fwhyn.myapplication.domain.model.Module
import javax.inject.Inject

// Repository classes are responsible for the following tasks:
// - Exposing data to the rest of the app.
// - Centralizing changes to the data.
// - Resolving conflicts between multiple data sources.
// - Abstracting sources of data from the rest of the app.
// - Containing business logic.

class ModuleRepositoryImpl @Inject constructor(private val moduleLocalDataSource: ModuleLocalDataSource) : ModuleRepository{
    override fun getModules(): List<Module> {
        // if else for example
        if (true) {
            return moduleLocalDataSource.moduleList
        } else {
            return emptyList()
        }
    }

}