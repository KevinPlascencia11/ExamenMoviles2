package com.example.examen2.Others

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen2.Screens.FavoriteScreen
import com.example.examen2.Screens.MainScreen
import com.example.examen2.Screens.DetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() //Controlador de navegación
    val items = listOf(
        Screen.Home,
        Screen.Favorites
    )
    var selectedItem by remember { mutableStateOf(0) } //Estado del ítem seleccionado en la barra de navegación

    Scaffold(
        bottomBar = {
            BottomNavigation {
                items.forEachIndexed { index, screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = if (index == 0) Icons.Filled.Home else Icons.Filled.Star,
                                contentDescription = null
                            )
                        },
                        label = { Text(screen.label) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true //Guardar el estado de la navegación
                                }
                                launchSingleTop = true //Evitar crear múltiples copias del mismo destino
                                restoreState = true //Restaurar el estado de la navegación previa
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        //Configuración de las rutas de navegación
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                MainScreen(
                    onShowClick = { showId ->
                        navController.navigate("detail/$showId") //Navegar a la pantalla de detalles del show
                    },
                    onFavoritesClick = {
                        navController.navigate("favorites") //Navegar a la pantalla de favoritos
                    }
                )
            }
            composable(Screen.Favorites.route) {
                FavoriteScreen(
                    navController = navController,
                    onShowClick = { showId ->
                        navController.navigate("detail/$showId") //Navegar a la pantalla de detalles del show desde favoritos
                    }
                )
            }
            composable("detail/{showId}") { backStackEntry ->
                val showId = backStackEntry.arguments?.getString("showId")?.toIntOrNull()
                showId?.let {
                    DetailScreen(navController, it) //Mostrar la pantalla de detalles del show
                }
            }
        }
    }
}

//Clases selladas para definir las rutas y etiquetas de las pantallas
sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Home")
    object Favorites : Screen("favorites", "Favorites")
}
