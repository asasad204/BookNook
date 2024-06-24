package com.example.bookstore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.bookstore.MyViewModel
import com.example.bookstore.model.Book

@Composable
fun InfoScreen(navController: NavController, isbn13: String, viewModel: MyViewModel = viewModel()) {
    val bookInfoState = viewModel.fetchBookInfo(isbn13).value


    bookInfoState?.let { book ->
        Card(
            colors = CardDefaults.cardColors(Color(red = 225, green = 217, blue = 191)),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(text = book.title ?: "", style = MaterialTheme.typography.titleLarge)
                Text(text = book.subtitle ?: "", style = MaterialTheme.typography.titleMedium)
                Text(text = book.authors ?: "", style = MaterialTheme.typography.titleSmall)
                //Text(text = book.publisher ?: "", style = MaterialTheme.typography.bodyMedium)
                //Text(text = book.language ?: "", style = MaterialTheme.typography.bodySmall)
                //Text(text = book.isbn13 ?: "", style = MaterialTheme.typography.bodySmall)
                Text(text = book.price ?: "", style = MaterialTheme.typography.bodySmall)

                book.image?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(300.dp)
                    )
                }

                Text(text = book.desc ?: "", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
