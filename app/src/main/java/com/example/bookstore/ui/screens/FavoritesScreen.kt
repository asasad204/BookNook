package com.example.bookstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookstore.BookCard
import com.example.bookstore.MyViewModel
import com.example.bookstore.model.Book

@Composable
fun FavoritesScreen(navController: NavController, viewModel: MyViewModel = viewModel()) {
    val favoriteBooks = viewModel.favoriteBooks.observeAsState().value
    val books = favoriteBooks ?: emptyList()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Favorites", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(books) { bookInfo ->
                    BookCard(
                        book = Book(
                            title = bookInfo.title,
                            subtitle = bookInfo.subtitle,
                            isbn13 = bookInfo.isbn13,
                            price = bookInfo.price,
                            image = bookInfo.image,
                            url = bookInfo.url
                        ),
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

    }
}

