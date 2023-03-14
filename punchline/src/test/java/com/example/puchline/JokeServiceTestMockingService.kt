package com.example.puchline

import com.fwhyn.punchline.*
import io.reactivex.Single
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class JokeServiceTestMockingService {
    private val jokeService: JokeService = mock()
    private val repository = RepositoryImpl(jokeService)

    @Test
    fun getRandomJokeEmitsJoke() {
        // 1
        val joke = Joke(id, joke)
        // 2
        whenever(jokeService.getRandomJoke()).thenReturn(Single.just(joke))
        // 3
        val testObserver = repository.getJoke().test()
        // 4
        testObserver.assertValue(joke)
    }
}