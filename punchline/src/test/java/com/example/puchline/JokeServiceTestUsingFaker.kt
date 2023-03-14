package com.example.puchline

import com.fwhyn.punchline.Joke
import com.fwhyn.punchline.JokeService
import com.fwhyn.punchline.RepositoryImpl
import com.github.javafaker.Faker
import io.reactivex.Single
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class JokeServiceTestUsingFaker {
    var faker = Faker()
    private val jokeService: JokeService = mock()
    private val repository = RepositoryImpl(jokeService)

    @Test
    fun getRandomJokeEmitsJoke() {
        val joke = Joke(faker.idNumber().valid(), faker.lorem().sentence())

        whenever(jokeService.getRandomJoke()).thenReturn(Single.just(joke))
        val testObserver = repository.getJoke().test()

        testObserver.assertValue(joke)
    }
}