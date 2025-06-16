package com.example.yourday.database.dao.finance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalBalanceType
import kotlinx.coroutines.flow.Flow

// Balance Types
@Dao
interface BalanceTypeDao {
    @Upsert
    suspend fun upsert(type: LocalBalanceType)

    @Delete
    suspend fun delete(type: LocalBalanceType)

    @Query("SELECT * FROM balance_types")
    fun getAll(): Flow<List<LocalBalanceType>>

    @Query("SELECT * FROM balance_types WHERE id = :id")
    suspend fun getById(id: Int): LocalBalanceType?

    @Query("DELETE FROM balance_types")
    suspend fun deleteAllBalanceTypes()
}