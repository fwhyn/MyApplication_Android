package com.fwhyn.myapplication.data

import com.fwhyn.myapplication.domain.model.Module

interface ModuleRepository {
    fun getModules(): List<Module>
}