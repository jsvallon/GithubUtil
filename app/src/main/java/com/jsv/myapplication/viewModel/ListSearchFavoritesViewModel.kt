package com.jsv.myapplication.viewModel



import android.app.Application
import androidx.lifecycle.*
import com.jsv.myapplication.database.DatabaseSearch
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.repository.SearchRepository
import com.jsv.myapplication.service.ItemsResult
import io.reactivex.Flowable


class ListSearchFavoritesViewModel(
    val database: GitHubDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


    //Favorite functions
    private val searchRepository = SearchRepository(database)

    private val _listFavorites = MutableLiveData<List<ItemsResult>>()
    val listFavorites : LiveData<List<ItemsResult>>
    get() = _listFavorites



    fun getListFavorites() : Flowable<List<DatabaseSearch>>{
         return searchRepository.getListFavorites()
    }

    fun updateListFavorites(favorites: List<ItemsResult>) {
        _listFavorites.value = favorites
    }
}
