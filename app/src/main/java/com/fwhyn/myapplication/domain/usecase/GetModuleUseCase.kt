package com.fwhyn.myapplication.domain.usecase

import com.fwhyn.myapplication.data.ModuleRepository
import com.fwhyn.myapplication.domain.model.Module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class GetModuleUseCase(private val moduleRepository: ModuleRepository) : BaseUseCase<Unit, List<Module>>() {
    override fun execute(param: Unit, coroutineScope: CoroutineScope) {
        run(coroutineScope) {
            delay(2000)
            return@run moduleRepository.get(Unit)
        }
    }
}