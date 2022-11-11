package com.fwhyn.myapplication.cocktails.game.factory

import com.fwhyn.myapplication.cocktails.common.network.Cocktail
import com.fwhyn.myapplication.cocktails.common.repository.CocktailsRepository
import com.fwhyn.myapplication.cocktails.common.repository.RepositoryCallback
import com.fwhyn.myapplication.cocktails.game.model.Game
import com.fwhyn.myapplication.cocktails.game.model.Score

class CocktailsGameFactoryImpl(
    private val repository: CocktailsRepository
) : CocktailsGameFactory {

    override fun buildGame(callback: CocktailsGameFactory.Callback) {
        repository.getAlcoholic(
            object : RepositoryCallback<List<Cocktail>, String> {
                override fun onSuccess(cocktailList: List<Cocktail>) {
                    val score = Score(repository.getHighScore())
                    val game = Game(emptyList(), score)
                    callback.onSuccess(game)
                }

                override fun onError(e: String) {
                    callback.onError()
                }
            })
    }
}