package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalActivityType
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityTypeDao {
    @Upsert
    suspend fun upsert(type: LocalActivityType)

    @Delete
    suspend fun delete(type: LocalActivityType)

    @Query("SELECT * FROM activity_types")
    fun getAll(): Flow<List<LocalActivityType>>

    @Query("SELECT * FROM activity_types WHERE id = :id")
    suspend fun getById(id: Int): LocalActivityType?

    @Query("DELETE FROM activity_types")
    suspend fun deleteAll()


}
