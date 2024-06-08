package com.example.examen2.Others

import android.content.Context
import com.example.examen2.data.database.AppDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//Define una interfaz para el contenedor de la aplicación que contiene el repositorio de programas
interface AppContainer {
    //Propiedad abstracta que debe ser implementada para proporcionar un repositorio de programas
    val showsRepository: ShowsRepository
}

//Implementación por defecto del contenedor de la aplicación que inicializa y proporciona las dependencias necesarias
class DefaultAppContainer(context: Context) : AppContainer {
    //URL base para las peticiones a la API de TVMaze
    private val BASE_URL = "https://api.tvmaze.com/"

    //Configuración de Moshi para la serialización/deserialización de JSON
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //Configuración de Retrofit para realizar peticiones HTTP
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    //Servicio de Retrofit que se inicializa de forma perezosa para las peticiones a la API
    private val retrofitService: ShowsApiService by lazy {
        retrofit.create(ShowsApiService::class.java)
    }

    //Base de datos de la aplicación que se inicializa de forma perezosa
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    //Repositorio de programas que se inicializa de forma perezosa y utiliza el servicio de Retrofit y DAO de la base de datos
    override val showsRepository: ShowsRepository by lazy {
        ShowsRepository(retrofitService, database.showDao())
    }
}
