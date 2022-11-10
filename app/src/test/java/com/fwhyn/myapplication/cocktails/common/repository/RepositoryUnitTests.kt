package com.fwhyn.myapplication.cocktails.common.repository

import android.content.SharedPreferences
import com.fwhyn.myapplication.cocktails.common.network.CocktailsApi
import org.junit.Test
import org.mockito.kotlin.*

class RepositoryUnitTests {

    @Test
    fun saveScore_shouldSaveToSharedPreferences() {
        val api: CocktailsApi = mock()
        // 1
        val sharedPreferencesEditor: SharedPreferences.Editor =
            mock()
        val sharedPreferences: SharedPreferences = mock()
        whenever(sharedPreferences.edit())
            .thenReturn(sharedPreferencesEditor)
        val repository = CocktailsRepositoryImpl(
            api,
            sharedPreferences
        )

        // 2
        val score = 100
        repository.saveHighScore(score)

        // 3
        inOrder(sharedPreferencesEditor) {
            // 4
            verify(sharedPreferencesEditor).putInt(any(), eq(score))
            verify(sharedPreferencesEditor).apply()
        }
    }
}