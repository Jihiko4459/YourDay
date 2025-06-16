package com.example.yourday.factory.finance

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.finance.TransactionRepository
import com.example.yourday.viewmodel.finance.TransactionViewModel

class TransactionViewModelFactory(
    val app: Application,
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionViewModel(app, repository) as T
    }
}
