package com.example.bookstore


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookstore.model.Favorite
import com.example.bookstore.model.FavoriteDao

@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase(){

    companion object {
        const val NAME = "Favorite_DB"
    }

    abstract fun getFavoriteDao() : FavoriteDao

}