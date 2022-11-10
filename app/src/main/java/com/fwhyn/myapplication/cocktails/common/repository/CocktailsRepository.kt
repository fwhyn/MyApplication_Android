package com.fwhyn.myapplication.cocktails.common.repository

interface CocktailsRepository {
    fun saveHighScore(score: Int)
}