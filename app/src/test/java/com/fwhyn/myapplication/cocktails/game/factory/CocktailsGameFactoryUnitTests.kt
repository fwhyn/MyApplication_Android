package com.fwhyn.myapplication.cocktails.game.factory

import com.fwhyn.myapplication.cocktails.common.network.Cocktail
import com.fwhyn.myapplication.cocktails.common.repository.CocktailsRepository
import com.fwhyn.myapplication.cocktails.common.repository.RepositoryCallback
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CocktailsGameFactoryUnitTests {

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory

    @Before
    fun setup() {
        repository = mock()
        factory = CocktailsGameFactoryImpl(repository)
    }

    @Test
    fun buildGame_shouldGetCocktailsFromRepo() {
        factory.buildGame(mock())

        verify(repository).getAlcoholic(any())
    }

    private val cocktails = listOf(
        Cocktail("1", "Drink1", "image1"),
        Cocktail("2", "Drink2", "image2"),
        Cocktail("3", "Drink3", "image3"),
        Cocktail("4", "Drink4", "image4")
    )

    @Test
    fun buildGame_shouldCallOnSuccess() {
        val callback = mock<CocktailsGameFactory.Callback>()
        setUpRepositoryWithCocktails(repository)

        factory.buildGame(callback)

        verify(callback).onSuccess(any())
    }

    private fun setUpRepositoryWithCocktails(
        repository: CocktailsRepository) {
        doAnswer {
            // 1
            val callback: RepositoryCallback<List<Cocktail>, String>
                    = it.getArgument(0)
            callback.onSuccess(cocktails)
        }.whenever(repository).getAlcoholic(any())
    }
}