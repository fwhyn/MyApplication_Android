package com.fwhyn.myapplication.di

import com.fwhyn.myapplication.data.local.ModuleLocalDataSource
import com.fwhyn.myapplication.data.repository.CoroutineBaseRepository
import com.fwhyn.myapplication.data.repository.ModuleRepository
import com.fwhyn.myapplication.domain.model.ModuleModel
import com.fwhyn.myapplication.domain.usecase.BaseUseCase
import com.fwhyn.myapplication.domain.usecase.GetModuleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideGetModuleUseCase(moduleRepository: CoroutineBaseRepository<Unit, List<ModuleModel>>):
            BaseUseCase<Unit, List<ModuleModel>> {
        return GetModuleUseCase(moduleRepository)
    }

    @Provides
    fun provideModuleRepository(moduleLocalDataSource: ModuleLocalDataSource):
            CoroutineBaseRepository<Unit, List<ModuleModel>> {
        return ModuleRepository(moduleLocalDataSource)
    }
}