package com.fwhyn.cocktails.common.repository

import com.fwhyn.cocktails.common.network.Cocktail

interface CocktailsRepository {
    fun getAlcoholic(callback: RepositoryCallback<List<Cocktail>, String>)
    fun saveHighScore(score: Int)
    fun getHighScore(): Int
}

interface RepositoryCallback<T, E> {
    fun onSuccess(t: T)
    fun onError(e: E)
}