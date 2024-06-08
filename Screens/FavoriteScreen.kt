package com.example.examen2.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.examen2.Others.ViewStatus

//Pantalla de favoritos que muestra una lista de programas de TV marcados como favoritos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    onShowClick: (Int) -> Unit = {},
    viewModel: ViewStatus = viewModel(factory = ViewStatus.Factory)
) {
    //Estado de la UI obtenido del ViewModel
    val state by viewModel.status

    //Efecto lanzado al inicio para obtener los programas favoritos
    LaunchedEffect(Unit) { viewModel.getAllFavoriteShows() }

    //Columna que contiene todos los elementos de la UI
    Column {
        //Botón para volver a la pantalla anterior
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        //Condición para mostrar texto si la lista de favoritos está vacía
        if (state.favoritesShow.isEmpty()) {
            Text(
                text = "Empty",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(100.dp)
            )
        } else {
            //Cuadrícula perezosa para mostrar los programas favoritos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                content = {
                    items(state.favoritesShow) { favoriteShow ->
                        //Tarjeta para cada programa favorito
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .height(300.dp)
                                .clickable { favoriteShow.id?.let { onShowClick(it) } }
                        ) {
                            //Columna para el contenido de la tarjeta
                            Column {
                                //Caja para la miniatura
                                Box(
                                    modifier = Modifier.weight(3f)
                                ) {
                                    AsyncImage(
                                        model = favoriteShow.image,
                                        contentDescription = "Image of ${favoriteShow.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                    //Caja para la calificación
                                    favoriteShow.rate?.let {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = it.toString(),
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .clip(MaterialTheme.shapes.small)
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(4.dp)
                                            )
                                        }
                                    }
                                }
                                //Columna para el título y los datos
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = favoriteShow.name.toString(),
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)
                                    )
                                    //Géneros del programa
                                    favoriteShow.genres?.let {
                                        Text(
                                            text = it,
                                            fontSize = 8.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}
