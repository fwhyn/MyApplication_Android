package com.fwhyn.myapplication.data.repository

import com.fwhyn.myapplication.data.local.ModuleLocalDataSource
import com.fwhyn.myapplication.domain.model.ModuleModel
import javax.inject.Inject

class ModuleRepository @Inject constructor(private val moduleLocalDataSource: ModuleLocalDataSource) :
    CoroutineBaseRepository<Unit, List<ModuleModel>> {
    override suspend fun get(param: Unit): List<ModuleModel> {
        // if else for example
        if (true) {
            return moduleLocalDataSource.moduleModelLists
        } else {
            return emptyList()
        }
    }

    override suspend fun set(param: Unit, data: List<ModuleModel>) {
        TODO("Not yet implemented")
    }

}