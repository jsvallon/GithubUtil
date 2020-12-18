package com.jsv.myapplication

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jsv.myapplication.database.DatabaseSearch
import com.jsv.myapplication.database.GitHubDatabase
import com.jsv.myapplication.database.GitHubDatabaseDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertEquals



@RunWith(AndroidJUnit4::class)
class GitHubDatabaseTest {

    private lateinit var githubDao: GitHubDatabaseDao
    private lateinit var dbconnect: GitHubDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        dbconnect = Room.inMemoryDatabaseBuilder(context, GitHubDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        githubDao = dbconnect.gitHubDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        dbconnect.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertFavorite() {
      val favorite = DatabaseSearch(8,"Rapho",null, "Bon bagay","Espagnol")
        githubDao.insertFavorite(favorite)
        val singleFavorite =  githubDao.getSingleFavorite()
        assertEquals(singleFavorite?.id, 8)
    }


}