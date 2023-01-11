package com.fwhyn.wishlist.persistance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fwhyn.wishlist.Wishlist
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class WishlistDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wishlistDatabase: WishlistDatabase
    private lateinit var wishlistDao: WishlistDao

    @Before
    fun initDb() {
        // 1
        wishlistDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WishlistDatabase::class.java).build()
        // 2
        wishlistDao = wishlistDatabase.wishlistDao()
    }

    @Test
    fun getAllReturnsEmptyList() {
        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }


    @After
    fun closeDb() {
        wishlistDatabase.close()
    }
}