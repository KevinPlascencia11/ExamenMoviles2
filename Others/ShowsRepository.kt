package com.example.examen2.Others

import com.example.examen2.data.database.FavoriteShow
import com.example.examen2.data.database.ShowDao
import kotlinx.coroutines.flow.first

class ShowsRepository(
    private val showsApiService: ShowsApiService, //Servicio API para obtener datos de shows
    private val showDao: ShowDao //DAO para operaciones de base de datos relacionadas con los shows
) {
    //Función para obtener la lista de shows desde el servicio API
    suspend fun getShows(): List<Show> = showsApiService.getShows()

    //Función para obtener un show específico por su ID desde el servicio API
    suspend fun getShowById(id: Int): Show = showsApiService.getShowById(id)

    //Función para obtener una lista de shows por nombre desde el servicio API
    suspend fun getShowsByName(name: String): List<Show> {
        val response = showsApiService.getShowsByName(name)
        return response.map { it.show }
    }

    //Función para agregar un show a la lista de favoritos en la base de datos
    suspend fun addFavoriteShow(favoriteShow: FavoriteShow) {
        showDao.insertFavoriteShow(favoriteShow)
    }

    //Función para obtener todos los shows favoritos desde la base de datos
    suspend fun getAllFavoriteShows(): List<FavoriteShow> {
        return showDao.getAll().first()
    }

    //Función para eliminar un show de la lista de favoritos en la base de datos
    suspend fun removeFavoriteShow(favoriteShow: FavoriteShow) {
        showDao.deleteFavoriteShow(favoriteShow)
    }

    //Función para verificar si un show es favorito por su ID en la base de datos
    suspend fun isFavoriteShow(id: Int): Boolean {
        return showDao.getFavoriteShowById(id) != null
    }
}
