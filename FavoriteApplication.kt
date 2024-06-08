package com.example.examen2

import android.app.Application
import com.example.examen2.Others.AppContainer
import com.example.examen2.Others.DefaultAppContainer
import com.example.examen2.data.database.AppDatabase

class FavoriteApplication : Application() {
    //Contenedor de la aplicación para manejar la inyección de dependencias
    lateinit var container: AppContainer

    //Base de datos de la aplicación accesible de manera estática
    companion object {
        lateinit var appDatabase: AppDatabase
    }

    //Método llamado cuando la aplicación es creada
    override fun onCreate() {
        super.onCreate()
        //Inicialización del contenedor de dependencias
        container = DefaultAppContainer(this)

        //Inicialización de la base de datos de la aplicación
        appDatabase = AppDatabase.getInstance(this)
    }
}
