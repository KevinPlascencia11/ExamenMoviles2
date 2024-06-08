package com.example.examen2.Others

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Interfaz para el servicio de API que maneja las peticiones relacionadas con programas de TV
interface ShowsApiService {

    //Obtiene una lista de todos los programas de TV disponibles
    @GET("shows")
    suspend fun getShows(): List<Show>

    //Obtiene un programa de TV específico por su ID, incluyendo el reparto en la respuesta
    @GET("shows/{id}?embed=cast")
    suspend fun getShowById(@Path("id") id: Int): Show

    //Busca programas de TV por nombre y devuelve una lista de respuestas de búsqueda
    @GET("search/shows")
    suspend fun getShowsByName(@Query("q") name: String): List<SearchShowResponse>

}
