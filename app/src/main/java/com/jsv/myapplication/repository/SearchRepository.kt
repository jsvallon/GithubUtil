package com.jsv.myapplication.repository


import com.jsv.myapplication.database.DatabaseSearch
import com.jsv.myapplication.database.GitHubDatabaseDao
import io.reactivex.Completable
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository (private val database: GitHubDatabaseDao) {


    fun getListFavorites(): Flowable<List<DatabaseSearch>> {
        return database.getAllFavFlow()
    }


    //return dataSource.insertUser(user)

//    suspend fun insertFavorite(databaseSearch: DatabaseSearch) {
//        withContext(Dispatchers.IO) {
//            if (databaseSearch.isFavorite) {
//                database.insertFavorite(databaseSearch)
//            } else {
//                databaseSearch.id?.let { database.deleteFavoriteById(it) }
//            }
//            //database.update(databaseSearch)
//        }
//    }


    fun insertFavorite(databaseSearch: DatabaseSearch) {
        if (databaseSearch.isFavorite) {
               database.insertFavorite(databaseSearch)
        } else {
            databaseSearch.id?.let { database.deleteFavoriteById(it) }
        }
    }


    suspend fun deleteFavorite(id: Int) {
        withContext(Dispatchers.IO) {
            database.deleteFavoriteById(id)
        }
    }
}