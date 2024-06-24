package com.example.bookstore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstore.Screen
import com.example.bookstore.R
@Composable
fun LandingScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.library),
            contentDescription = "Cover Image",
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(alpha = 0.8f),
            contentScale = ContentScale.FillBounds

        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.navlogo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            Button(
                onClick = { navController.navigate(Screen.Discover.route) },
                colors = ButtonDefaults.buttonColors(Color(red = 225, green = 217, blue = 191))
                ) {
                Text(text = "Discover Books", color = Color.Black)
            }
        }
    }
}
