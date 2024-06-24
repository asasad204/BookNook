package com.example.bookstore.model

data class BookResponse(
    val error: String,
    val total: String,
    val books: List<Book>
)
