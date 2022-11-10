package com.fwhyn.myapplication.cocktails.common.repository

import android.content.SharedPreferences
import com.fwhyn.myapplication.cocktails.common.network.CocktailsApi
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RepositoryUnitTests {
    private lateinit var repository: CocktailsRepository
    private lateinit var api: CocktailsApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        api = mock()
        sharedPreferences = mock()
        sharedPreferencesEditor = mock()
        whenever(sharedPreferences.edit())
            .thenReturn(sharedPreferencesEditor)

        repository = CocktailsRepositoryImpl(api, sharedPreferences)
    }

    @Test
    fun saveScore_shouldSaveToSharedPreferences() {
        val score = 100
        repository.saveHighScore(score)

        inOrder(sharedPreferencesEditor) {
            verify(sharedPreferencesEditor).putInt(any(), eq(score))
            verify(sharedPreferencesEditor).apply()
        }
    }

    @Test
    fun getScore_shouldGetFromSharedPreferences() {
        repository.getHighScore()

        verify(sharedPreferences).getInt(any(), any())
    }

    @Test
    fun saveScore_shouldNotSaveToSharedPreferencesIfLower() {
        val previouslySavedHighScore = 100
        val newHighScore = 10
        val spyRepository = spy(repository)
        doReturn(previouslySavedHighScore)
            .whenever(spyRepository)
            .getHighScore()

        spyRepository.saveHighScore(newHighScore)

        verify(sharedPreferencesEditor, never())
            .putInt(any(), eq(newHighScore))
    }

}