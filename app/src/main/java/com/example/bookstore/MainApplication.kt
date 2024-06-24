package com.example.bookstore

import android.app.Application
import androidx.room.Room
import com.example.bookstore.FavoritesDatabase

class MainApplication : Application() {

    companion object {
        private lateinit var _favoritesDatabase: FavoritesDatabase
        val favoritesDatabase: FavoritesDatabase
            get() = _favoritesDatabase

        fun initializeDatabase(application: Application) {
            _favoritesDatabase = Room.databaseBuilder(
                application.applicationContext,
                FavoritesDatabase::class.java,
                FavoritesDatabase.NAME
            ).build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializeDatabase(this)
    }
}
