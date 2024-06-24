
package com.example.bookstore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.model.Book
import com.example.bookstore.ui.screens.DiscoverScreen
import com.example.bookstore.ui.screens.FavoritesScreen
import com.example.bookstore.ui.screens.InfoScreen
import com.example.bookstore.ui.screens.LandingScreen
import com.example.bookstore.ui.screens.SelectScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object Discover : Screen("discover")
    data object Favorites : Screen("favorites")
    data object Select : Screen("select/{query}") {
        fun createRoute(query: String) = "select/$query"
    }

    data object Info : Screen("info/{isbn13}") {
        fun createRoute(isbn13: String) = "info/$isbn13"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookStoreNavHost(navController: NavHostController = rememberNavController(), viewModel: MyViewModel= viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerContent = {


                    ModalDrawerSheet(
                        modifier = Modifier.background(color = Color.Blue)
                    ) {
                            AnimatedVisibility(visible = drawerState.isOpen) {
                                Column(
                                    modifier = Modifier.padding(2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.navlogo),
                                        contentDescription = "App Logo",
                                        modifier = Modifier.size(200.dp)
                                    )
                                }
                            }

                            //Spacer(modifier = Modifier.height(16.dp))
                            NavigationDrawerItem(
                                label = { Text("Discover") },
                                selected = false,
                                onClick = {
                                    navController.navigate(Screen.Discover.route)
                                    scope.launch { drawerState.close() }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_grain),
                                        contentDescription = "Discover Icon",
                                        tint = Color(red = 233, green = 146, blue = 0)
                                    )
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Favorites") },
                                selected = false,
                                onClick = {
                                    navController.navigate(Screen.Favorites.route)
                                    scope.launch { drawerState.close() }
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = "Favorites",
                                        tint = Color(red = 233, green = 146, blue = 0)
                                    )
                                }
                            )

                    }

            },
            drawerState = drawerState
        ) {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            Scaffold(
                topBar = {
                    if (currentRoute != Screen.Landing.route) {
                        TopAppBar(
                            title = { Text("BookNook") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Landing.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Landing.route) { LandingScreen(navController) }
                    composable(Screen.Discover.route) { DiscoverScreen(navController, viewModel) }
                    composable(Screen.Select.route) { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query")
                        if (query != null) {
                            SelectScreen(navController, query)
                        }
                    }
                    composable(Screen.Info.route) { backStackEntry ->
                        val isbn13 = backStackEntry.arguments?.getString("isbn13")
                        if (isbn13 != null) {
                            InfoScreen(navController, isbn13)
                        }
                    }
                    composable(Screen.Favorites.route) { FavoritesScreen(navController) }

                }
            }
        }

}
@Composable
fun BookStoreApp(navController: NavController, modifier: Modifier = Modifier, viewModel: MyViewModel = viewModel()) {
    val bookResponse = viewModel.booksData.observeAsState().value
    val books = bookResponse ?: emptyList()
    val configuration = LocalConfiguration.current

    if (books.isNotEmpty()) {
        if (configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            LazyColumn(modifier = modifier.padding(16.dp)) {
                items(books) { book ->
                    BookCard(book = book, navController = navController, viewModel = viewModel)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = modifier.padding(16.dp)
            ) {
                items(books) { book ->
                    BookCard(book = book, navController = navController, viewModel = viewModel)
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

@Composable
fun BookCard(
    book: Book,
    navController: NavController,
    viewModel: MyViewModel,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(book.isbn13) {
        isFavorite = viewModel.isFavorite(book.isbn13)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(Screen.Info.createRoute(book.isbn13))
            },
        colors = CardDefaults.cardColors(Color(red = 225, green = 217, blue = 191))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = book.image),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.size(width = 18.dp, height = 4.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)

            ) {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = book.subtitle, style = MaterialTheme.typography.labelMedium)
                Text(text = book.url, style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                imageVector = if (isFavorite) Icons.Default.Delete else Icons.Default.Favorite,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        coroutineScope.launch {
                            viewModel.toggleFavorite(book.isbn13, isFavorite)
                            isFavorite = !isFavorite
                        }
                    }
            )
        }
    }
}
