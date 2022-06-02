package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newsEntity: List<NewsEntity>)

    @Delete
    suspend fun remove(newsEntity: NewsEntity)

    @Update fun update(newsEntity: NewsEntity)

    @Query("SELECT * FROM NewsEntity")
    fun getNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM NewsEntity WHERE isFavorite=1")
    fun getFavorites(): Flow<List<NewsEntity>>

}