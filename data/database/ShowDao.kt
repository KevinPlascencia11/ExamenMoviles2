package com.example.examen2.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<FavoriteShow>>

    @Query("SELECT COUNT(*) FROM favorites")
    fun getCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteShow(favoriteShow: FavoriteShow)

    @Delete
    suspend fun deleteFavoriteShow(favoriteShow: FavoriteShow)

    @Query("SELECT * FROM favorites WHERE id = :showId")
    suspend fun getFavoriteShowById(showId: Int): FavoriteShow?
}