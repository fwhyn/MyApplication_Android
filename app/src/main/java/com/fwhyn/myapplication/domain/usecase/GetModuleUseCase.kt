package com.fwhyn.myapplication.domain.usecase

import com.fwhyn.myapplication.data.repository.CoroutineBaseRepository
import com.fwhyn.myapplication.domain.model.ModuleModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class GetModuleUseCase(private val moduleModelRepository: CoroutineBaseRepository<Unit, List<ModuleModel>>) :
    BaseUseCase<Unit, List<ModuleModel>>() {
    override fun execute(param: Unit, coroutineScope: CoroutineScope) {
        run(coroutineScope) {
            delay(2000)
            return@run moduleModelRepository.get(Unit)
        }
    }
}