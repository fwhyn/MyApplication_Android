package com.fwhyn.myapplication.cocktails.game.factory

import com.fwhyn.myapplication.cocktails.game.model.Game

interface CocktailsGameFactory {

    fun buildGame(callback: Callback)

    interface Callback {
        fun onSuccess(game: Game)
        fun onError()
    }
}