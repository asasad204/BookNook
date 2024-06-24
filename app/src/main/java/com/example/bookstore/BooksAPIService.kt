package com.example.bookstore

import com.example.bookstore.model.BookInfo
import com.example.bookstore.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BooksAPIService {
    @GET("new")
    suspend fun getNewBooks() : BookResponse

    @GET("search/{query}")
    suspend fun searchBooks(@Path("query") query: String): BookResponse

    @GET("books/{isbn13}")
    suspend fun getInfo(@Path("isbn13") isbn13: String): BookInfo
}