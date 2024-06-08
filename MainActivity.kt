package com.example.examen2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.examen2.Others.AppNavigation
import com.example.examen2.ui.theme.Examen2Theme

class MainActivity : ComponentActivity() {
    //onCreate es el primer punto de entrada para la ejecución de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //Define la apariencia y el estilo de tu UI
            Examen2Theme {
                //Surface es un contenedor que proporciona color de fondo, elevación y forma para su contenido
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //AppNavigation es una función que debe manejar la navegación en tu aplicación
                    AppNavigation()
                }
            }
        }
    }
}

