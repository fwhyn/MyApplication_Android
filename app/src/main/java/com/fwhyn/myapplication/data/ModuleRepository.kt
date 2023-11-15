package com.fwhyn.myapplication.data

import com.fwhyn.myapplication.data.local.ModuleLocalDataSource
import com.fwhyn.myapplication.data.repository.CoroutineBaseRepository
import com.fwhyn.myapplication.domain.model.Module
import javax.inject.Inject

class ModuleRepository @Inject constructor(private val moduleLocalDataSource: ModuleLocalDataSource) :
    CoroutineBaseRepository<Unit, List<Module>> {
    override suspend fun get(param: Unit): List<Module> {
        // if else for example
        if (true) {
            return moduleLocalDataSource.moduleList
        } else {
            return emptyList()
        }
    }

    override suspend fun set(param: Unit, data: List<Module>) {
        TODO("Not yet implemented")
    }

}