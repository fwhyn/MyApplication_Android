package com.fwhyn.cocktails.game.factory

import com.fwhyn.cocktails.game.model.Game

interface CocktailsGameFactory {

    fun buildGame(callback: Callback)

    interface Callback {
        fun onSuccess(game: Game)
        fun onError()
    }
}