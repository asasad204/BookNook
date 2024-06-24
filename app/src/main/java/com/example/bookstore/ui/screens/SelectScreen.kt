package com.example.bookstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookstore.BookCard
import com.example.bookstore.MyViewModel


@Composable
fun SelectScreen(navController: NavController, query: String, viewModel: MyViewModel = viewModel()) {
    val books = viewModel.searchBooks(query).observeAsState().value ?: emptyList()
    val configuration = LocalConfiguration.current

    if (books.isNotEmpty()) {
        if (configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(books) { book ->
                    BookCard(book = book, navController = navController, viewModel)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(books) { book ->
                    BookCard(book = book, navController = navController, viewModel)
                }
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "No books found", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}