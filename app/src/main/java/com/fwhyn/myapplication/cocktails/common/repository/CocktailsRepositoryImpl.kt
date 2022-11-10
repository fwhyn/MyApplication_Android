package com.fwhyn.myapplication.cocktails.common.repository

import android.content.SharedPreferences
import com.fwhyn.myapplication.cocktails.common.network.Cocktail
import com.fwhyn.myapplication.cocktails.common.network.CocktailsApi

private const val HIGH_SCORE_KEY = "HIGH_SCORE_KEY"

class CocktailsRepositoryImpl(
    private val api: CocktailsApi,
    private val sharedPreferences: SharedPreferences
) : CocktailsRepository {

    override fun getAlcoholic(callback: RepositoryCallback<List<Cocktail>, String>) {
        TODO("Not yet implemented")
    }

    override fun saveHighScore(score: Int) {
        val highScore = getHighScore()
        if (score > highScore) {
            val editor = sharedPreferences.edit()
            editor.putInt(HIGH_SCORE_KEY, score)
            editor.apply()
        }
    }

    override fun getHighScore(): Int = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
}