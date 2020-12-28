package com.jsv.myapplication.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.viewModel.ListSearchFavoritesViewModel
import com.jsv.myapplication.viewModel.search.ListSearchViewModel


class ListSearchViewModelFactory(
    private val dataSource: GitHubDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListSearchViewModel::class.java)) {
            return ListSearchViewModel(
                dataSource,
                application
            ) as T
        }
        if (modelClass.isAssignableFrom(ListSearchFavoritesViewModel::class.java)) {
            return ListSearchFavoritesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}