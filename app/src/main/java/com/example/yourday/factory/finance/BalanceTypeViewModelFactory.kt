package com.example.yourday.factory.finance

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.finance.BalanceTypeRepository
import com.example.yourday.viewmodel.finance.BalanceTypeViewModel

class BalanceTypeViewModelFactory(
    val app: Application,
    private val repository: BalanceTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BalanceTypeViewModel(app) as T
    }
}