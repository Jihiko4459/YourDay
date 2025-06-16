package com.example.yourday.repository.finance

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalBalanceType
import kotlinx.coroutines.flow.Flow

class BalanceTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalBalanceType>> {
        return db.balanceTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalBalanceType? {
        return db.balanceTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalBalanceType) {
        db.balanceTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalBalanceType) {
        db.balanceTypeDao().delete(type)
    }

    suspend fun deleteAll() {
        db.balanceTypeDao().deleteAllBalanceTypes()
    }
}