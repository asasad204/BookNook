package com.example.bookstore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.Book
import com.example.bookstore.model.BookInfo
import com.example.bookstore.model.Favorite
import com.example.bookstore.model.FavoriteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {
    private val _booksData = MutableLiveData<List<Book>>()
    val booksData: LiveData<List<Book>> get() = _booksData

    private val _favoriteBooks = MutableLiveData<List<BookInfo>>()
    val favoriteBooks: LiveData<List<BookInfo>> get() = _favoriteBooks

    private val favoriteDao: FavoriteDao = MainApplication.favoritesDatabase.getFavoriteDao()
    private val _bookinfo = MutableLiveData<BookInfo?>()
    val bookinfo: LiveData<BookInfo?> get() = _bookinfo
    init {
        viewModelScope.launch {
            fetchFavoriteBooks()
            fetchNewBooks()
        }
    }

    private suspend fun fetchNewBooks() {
        try {
            val response = RetrofitClient.booksAPIService.getNewBooks()
            _booksData.value = response.books
        } catch (e: Exception) {

            _booksData.value = emptyList()
        }
    }

    private suspend fun fetchFavoriteBooks() {
        withContext(Dispatchers.IO) {
            val favoriteISBNs = favoriteDao.getAllFavoriteISBNs()
            val bookInfos = mutableListOf<BookInfo>()

            try {
                if (favoriteISBNs != null) {
                    for (isbn in favoriteISBNs) {
                        val info = RetrofitClient.booksAPIService.getInfo(isbn)
                        bookInfos.add(info)
                    }
                }


                withContext(Dispatchers.Main) {
                    _favoriteBooks.value = bookInfos
                }
            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    _favoriteBooks.value = bookInfos
                }
            }
        }
    }


    fun toggleFavorite(isbn13: String, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoriteDao.deleteFavorite(isbn13)

                _favoriteBooks.value = _favoriteBooks.value?.filter { it.isbn13 != isbn13 }
            } else {
                favoriteDao.addFavorite(Favorite(isbn13 = isbn13))

                val info = withContext(Dispatchers.IO) {
                    RetrofitClient.booksAPIService.getInfo(isbn13)
                }
                _favoriteBooks.value = _favoriteBooks.value.orEmpty() + info
            }
        }
    }

    suspend fun isFavorite(isbn13: String): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteDao.getFavorite(isbn13) != null
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            fetchFavoriteBooks()
            fetchNewBooks()
        }
    }
    fun searchBooks(query: String): LiveData<List<Book>> {
        val searchResults = MutableLiveData<List<Book>>()
        viewModelScope.launch {

                val response = RetrofitClient.booksAPIService.searchBooks(query)
                    searchResults.value = response.books
            }

        return searchResults
    }
    fun fetchBookInfo(isbn13: String) : LiveData<BookInfo?>{
        viewModelScope.launch {
            try {
                val info = withContext(Dispatchers.IO) {
                    RetrofitClient.booksAPIService.getInfo(isbn13)
                }
                _bookinfo.value = info
            } catch (e: Exception) {

                _bookinfo.value = null
            }
        }
        return bookinfo
    }
}



