package com.fwhyn.myapplication.data.di

import com.fwhyn.myapplication.data.ModuleRepository
import com.fwhyn.myapplication.data.ModuleRepositoryImpl
import com.fwhyn.myapplication.data.local.ModuleLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideModuleLocalDataSource(): ModuleLocalDataSource {
        return ModuleLocalDataSource()
    }

    @Provides
    fun provideModuleRepositoryImpl(moduleLocalDataSource: ModuleLocalDataSource): ModuleRepository {
        return ModuleRepositoryImpl(moduleLocalDataSource)
    }
}