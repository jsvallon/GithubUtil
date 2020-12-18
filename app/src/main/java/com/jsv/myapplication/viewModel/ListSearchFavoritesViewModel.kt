package com.jsv.myapplication.viewModel



import android.app.Application
import androidx.lifecycle.*
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.repository.SearchRepository

class ListSearchFavoritesViewModel(
    val database: GitHubDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


    //Favorite functions

    private val searchRepository = SearchRepository(database)

    val listFavorites = searchRepository.listFavorites

    override fun onCleared() {
        super.onCleared()
    }
}
