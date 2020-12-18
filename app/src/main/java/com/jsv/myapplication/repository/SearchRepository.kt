package com.jsv.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jsv.myapplication.database.DatabaseSearch
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.database.asItemDomainModel
import com.jsv.myapplication.service.ItemsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository (private val database: GitHubDatabaseDao) {

    val listFavorites: LiveData<List<ItemsResult>> = Transformations.map(database.getAllFavorites()){
        it.asItemDomainModel()
    }

    suspend fun insertFavorite(databaseSearch: DatabaseSearch) {
        withContext(Dispatchers.IO) {
            if (databaseSearch.isFavorite) {
                database.insertFavorite(databaseSearch)
            } else {
               databaseSearch.id?.let { database.deleteFavoriteById(it) }
            }
           //database.update(databaseSearch)
        }
    }

    suspend fun getData() : List<DatabaseSearch> {
        return withContext(Dispatchers.IO) {
             database.getAllFavorites2()
        }
    }

    suspend fun deleteFavorite(id: Int) {
        withContext(Dispatchers.IO) {
            database.deleteFavoriteById(id)
        }
    }
}