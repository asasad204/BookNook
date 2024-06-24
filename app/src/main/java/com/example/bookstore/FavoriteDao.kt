package com.example.bookstore.model

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE isbn13 = :isbn13")
    suspend fun deleteFavorite(isbn13: String)

    @Query("SELECT * FROM Favorite WHERE isbn13 = :isbn13 LIMIT 1")
    suspend fun getFavorite(isbn13: String): Favorite?

    @Query("SELECT isbn13 FROM Favorite")
    fun getAllFavoriteISBNs(): MutableList<String>?
}