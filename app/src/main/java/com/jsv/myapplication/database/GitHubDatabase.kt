package com.jsv.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseSearch::class], version = 3, exportSchema = false)
abstract class GitHubDatabase: RoomDatabase() {

    abstract val gitHubDatabaseDao : GitHubDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: GitHubDatabase ? = null

        fun getInstance(context: Context) : GitHubDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,GitHubDatabase::class.java,"github_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }

}