package com.example.examen2.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import coil.compose.AsyncImage
import com.example.examen2.Others.ViewStatus

//Pantalla principal que muestra una lista de programas de TV
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ViewStatus = viewModel(factory = ViewStatus.Factory),
    onShowClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    //Estado de la UI obtenido del ViewModel
    val state by viewModel.status
    //Texto de búsqueda gestionado por el estado
    var text by remember { mutableStateOf("") }

    //Efecto lanzado al inicio para obtener los programas
    LaunchedEffect(Unit) { viewModel.getShows() }

    //Columna que contiene todos los elementos de la UI
    Column {
        //Campo de texto para la búsqueda
        TextField(
            value = text,
            onValueChange = {
                text = it
                if (text.isEmpty()) {
                    viewModel.getShows()
                } else {
                    viewModel.getShowsByName(text)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            text = ""
                            viewModel.getShows()
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search Icon"
                    )
                }
            },
            placeholder = { Text(text = "Search") },
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors()
        )
        //Cuadrícula para mostrar los programas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(state.shows) { show ->
                //Tarjeta para cada programa
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(300.dp)
                        .clickable { show.id?.let { onShowClick(it) } }
                ) {
                    //Columna para el contenido de la tarjeta
                    Column {
                        // Caja para la miniatura.
                        Box(
                            modifier = Modifier.weight(3f)
                        ) {
                            AsyncImage(
                                model = show.image?.original,
                                contentDescription = "NA ${show.name ?: "NA"}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                            //Caja para la calificación
                            show.rating?.average?.let {
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
                                text = show.name ?: "NA",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            )
                            show.genres?.let {
                                Text(
                                    text = it.joinToString(separator = ", "),
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
    }
}
