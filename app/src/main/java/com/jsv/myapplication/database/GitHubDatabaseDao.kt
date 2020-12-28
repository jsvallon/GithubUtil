package com.jsv.myapplication.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface GitHubDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg favorite: DatabaseSearch)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertFavorite(vararg favorite: DatabaseSearch): Completable



    @Query("SELECT * from search_favorite WHERE id = :key")
    fun getFavoriteById(key: Int):DatabaseSearch

    @Query("DELETE FROM search_favorite WHERE id = :key")
    fun  deleteFavoriteById(key: Int );

    @Update
    fun update(favorite: DatabaseSearch) : Int

    @Query("SELECT * FROM search_favorite ORDER BY id DESC")
    fun getAllFavFlow() : Flowable<List<DatabaseSearch>>

    @Query("SELECT * FROM search_favorite ORDER BY id")
    fun getSingleFavorite(): DatabaseSearch?
}