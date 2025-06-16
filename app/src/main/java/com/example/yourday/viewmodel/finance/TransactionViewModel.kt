package com.example.yourday.viewmodel.finance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalTransaction
import com.example.yourday.repository.finance.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    application: Application,
    private val repository: TransactionRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _transactions = MutableStateFlow<List<LocalTransaction>>(emptyList())
    val transactions: StateFlow<List<LocalTransaction>> = _transactions.asStateFlow()

    fun loadTransactionsByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { transactionsList ->
                _transactions.value = transactionsList
            }
        }
    }

    fun upsertTransaction(transaction: LocalTransaction) {
        viewModelScope.launch {
            repository.upsert(transaction)
        }
    }

    fun deleteTransaction(transaction: LocalTransaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
}
