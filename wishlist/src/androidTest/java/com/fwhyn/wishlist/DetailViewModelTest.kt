package com.fwhyn.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.fwhyn.wishlist.persistance.RepositoryImpl
import com.fwhyn.wishlist.persistance.WishlistDao
import com.fwhyn.wishlist.persistance.WishlistDatabase
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // 1
    private val wishlistDao: WishlistDao = Mockito.spy(
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WishlistDatabase::class.java)
            .allowMainThreadQueries()
            .build().wishlistDao())

    // 2
    private val viewModel = DetailViewModel(RepositoryImpl(wishlistDao))

    @Test
    fun saveNewItemCallsDatabase() {
        // 1
        viewModel.saveNewItem(
            Wishlist(
                "Victoria",
                listOf("RW Android Apprentice Book", "Android phone"), 1
            ),
            "Smart watch"
        )
        // 2
        verify(wishlistDao).save(any())
    }

    @Test
    fun saveNewItemSavesData() {
        // 1
        val wishlist = Wishlist("Victoria", listOf("RW Android Apprentice Book", "Android phone"), 1)
        // 2
        val name = "Smart watch"
        viewModel.saveNewItem(wishlist, name)

        // 3
        val mockObserver = mock<Observer<Wishlist>>()
        // 4
        wishlistDao.findById(wishlist.id).observeForever(mockObserver)
        verify(mockObserver).onChanged(wishlist.copy(wishes = wishlist.wishes + name))
    }

    @Test
    fun getWishListCallsDatabase() {
        viewModel.getWishlist(1)

        verify(wishlistDao).findById(any())
    }

    @Test
    fun getWishListReturnsCorrectData() {
        // 1
        val wishlist = Wishlist("Victoria", listOf("RW Android Apprentice Book", "Android phone"), 1)
        // 2
        wishlistDao.save(wishlist)
        // 3
        val mockObserver = mock<Observer<Wishlist>>()
        viewModel.getWishlist(1).observeForever(mockObserver)
        // 4
        verify(mockObserver).onChanged(wishlist)
    }

}