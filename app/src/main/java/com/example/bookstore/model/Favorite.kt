package com.example.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite (
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    var isbn13: String
)