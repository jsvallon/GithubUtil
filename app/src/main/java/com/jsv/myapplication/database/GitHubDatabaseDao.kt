package com.jsv.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GitHubDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg favorite: DatabaseSearch)

    @Query("SELECT * from search_favorite WHERE id = :key")
    fun getFavoriteById(key: Int):DatabaseSearch

    @Query("DELETE FROM search_favorite WHERE id = :key")
    fun  deleteFavoriteById(key: Int );

    @Update
    fun update(favorite: DatabaseSearch) : Int

    @Query("SELECT * FROM search_favorite ORDER BY id DESC")
    fun getAllFavorites(): LiveData<List<DatabaseSearch>>

    @Query("SELECT * FROM search_favorite ORDER BY id DESC")
    fun getAllFavorites2(): List<DatabaseSearch>

    @Query("SELECT * FROM search_favorite ORDER BY id")
    fun getSingleFavorite(): DatabaseSearch?
}