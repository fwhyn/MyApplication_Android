package com.fwhyn.myapplication.cocktails.game.factory

import com.fwhyn.myapplication.cocktails.common.network.Cocktail
import com.fwhyn.myapplication.cocktails.common.repository.CocktailsRepository
import com.fwhyn.myapplication.cocktails.common.repository.RepositoryCallback
import com.fwhyn.myapplication.cocktails.game.model.Game

class CocktailsGameFactoryImpl(
    private val repository: CocktailsRepository
) : CocktailsGameFactory {

    override fun buildGame(callback: CocktailsGameFactory.Callback) {
        repository.getAlcoholic(
            object : RepositoryCallback<List<Cocktail>, String> {
                override fun onSuccess(cocktailList: List<Cocktail>) {
                    callback.onSuccess(Game(emptyList()))
                }

                override fun onError(e: String) {
                    // TODO
                }
            })
    }
}