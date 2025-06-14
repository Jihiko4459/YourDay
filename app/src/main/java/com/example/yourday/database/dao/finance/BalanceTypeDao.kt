package com.example.yourday.database.dao.finance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalBalanceType
import kotlinx.coroutines.flow.Flow

// Balance Types
@Dao
interface BalanceTypeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(type: LocalBalanceType)

    @Update
    suspend fun update(type: LocalBalanceType)

    @Delete
    suspend fun delete(type: LocalBalanceType)

    @Query("SELECT * FROM balance_types")
    fun getAll(): Flow<List<LocalBalanceType>>

    @Query("SELECT * FROM balance_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalBalanceType>
}