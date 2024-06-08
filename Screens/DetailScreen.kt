package com.example.examen2.Screens

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.examen2.Others.Show
import com.example.examen2.Others.ViewStatus

//Pantalla de detalles que muestra información detallada de un programa de TV seleccionado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    id: Int,
    viewModel: ViewStatus = viewModel(factory = ViewStatus.Factory),
) {
    //Estado de la UI obtenido del ViewModel
    val state by viewModel.status
    //Estado para manejar si el programa está marcado como favorito
    var isFavorite by remember { mutableStateOf(false) }
    //Icono que cambia dependiendo de si el programa es favorito o no
    var icon by remember { mutableStateOf(Icons.Filled.FavoriteBorder) }

    //Efecto lanzado al inicio para obtener los detalles del programa por ID y verificar si es favorito
    LaunchedEffect(Unit) {
        viewModel.getShowById(id)
        viewModel.isFavoriteShow(id) { result ->
            isFavorite = result
            icon = if (result) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        }
    }

    //Scaffold que proporciona una estructura básica de material design
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        //Caja que contiene todos los elementos de la UI
        Box(modifier = Modifier.fillMaxSize()) {
            //Muestra los detalles del programa seleccionado
            state.selectedShow?.let { show ->
                DetailContent(show, paddingValues)
            }
            //Botón para volver a la pantalla anterior
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(48.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            //Botón para marcar o desmarcar el programa como favorito
            IconButton(
                onClick = {
                    state.selectedShow?.toFavoriteShow()?.let { favoriteShow ->
                        if (isFavorite) viewModel.deleteFavoriteShow(favoriteShow)
                        else viewModel.addFavoriteShow(favoriteShow)
                        isFavorite = !isFavorite
                        icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(48.dp)
            ) {
                Icon(icon, contentDescription = "Favorite add/remove", tint = Color.Red)
            }
        }
    }
}

//Función Composable que construye el cuerpo de la pantalla de detalles
@Composable
fun DetailContent(show: Show, paddingValues: PaddingValues) {
    //Tarjeta que contiene la información detallada del programa
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        //Columna que permite el desplazamiento vertical
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            //Caja para la imagen del programa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = show.image?.original,
                    contentDescription = "Imagen del programa ${show.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            //Columna para el texto y detalles del programa
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                //Nombre del programa
                show.name?.let {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                //Calificación promedio del programa
                show.rating?.average?.let {
                    Box(
                        modifier = Modifier
                            .clip(AlertDialogDefaults.shape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(4.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = it.toString(),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                //Géneros del programa
                show.genres?.let {
                    DetailText("Genres: ${it.joinToString(", ")}", 0, 7)
                }
                //Fecha de estreno del programa
                DetailText("Premiered: ${show.premiered}", 0, 10)
                //País y código del país de la red del programa
                DetailText(
                    "Country: ${show.network?.country?.name}, ${show.network?.country?.code}",
                    0,
                    8
                )
                // Idioma del programa.
                DetailText("Language: ${show.language}", 0, 9)
                //Columna para la sinopsis del programa
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AlertDialogDefaults.shape)
                        .padding(10.dp)
                ) {
                    Text(
                        text = buildAnnotatedString { append(Html.fromHtml(show.summary)) },
                        fontSize = 15.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

//Función Composable para texto detallado con estilo
@Composable
fun DetailText(text: String, start: Int, end: Int) {
    //Texto con parte del contenido en negrita
    Text(
        text = AnnotatedString.Builder(text)
            .apply { addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end) }
            .toAnnotatedString(),
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}
