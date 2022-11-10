package com.fwhyn.myapplication.cocktails.common.repository

import android.content.SharedPreferences
import com.fwhyn.myapplication.cocktails.common.network.CocktailsApi

private const val HIGH_SCORE_KEY = "HIGH_SCORE_KEY"

class CocktailsRepositoryImpl(
    private val api: CocktailsApi,
    private val sharedPreferences: SharedPreferences
) : CocktailsRepository {

    override fun saveHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(HIGH_SCORE_KEY, score)
        editor.apply()
    }
}