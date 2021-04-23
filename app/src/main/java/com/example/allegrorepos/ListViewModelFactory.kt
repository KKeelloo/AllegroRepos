package com.example.allegrorepos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(private val username: String?,private val token: String?): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        return ListViewModel(username, token) as T
    }
}