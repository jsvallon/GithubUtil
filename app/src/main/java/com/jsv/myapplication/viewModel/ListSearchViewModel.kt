package com.jsv.myapplication.viewModel

import android.app.Application
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.repository.SearchRepository
import com.jsv.myapplication.service.ContainerItemsResult
import com.jsv.myapplication.service.GitHubApi
import com.jsv.myapplication.service.ItemsResult
import com.jsv.myapplication.service.asDomainModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSearchViewModel(val database: GitHubDatabaseDao, application: Application) :
    AndroidViewModel(application) {


    //Favorite functions
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val disposables = CompositeDisposable()


    private val searchRepository = SearchRepository(database)

    private val _search = MutableLiveData<List<ItemsResult>>()
    val search: LiveData<List<ItemsResult>>
        get() = _search


    val listFavorites = searchRepository.listFavorites

    val query: String? = null


    private val _favorites = MutableLiveData<List<ItemsResult>>()
    val favorites: LiveData<List<ItemsResult>>
        get() = _favorites


    fun insertFavorite(itemResult: ItemsResult) {
        uiScope.launch {
            val databaseEntity = itemResult.asDomainModel(itemResult)
            searchRepository.insertFavorite(databaseEntity)
        }
    }

    fun doneFetchingFavorite(itemsResult: List<ItemsResult>) {
        _favorites.value = itemsResult
    }

    private fun search(search: String) {
        GitHubApi.serviceRetrofit.search(search)
        .enqueue(object : Callback<ContainerItemsResult> {
            override fun onFailure(call: Call<ContainerItemsResult>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ContainerItemsResult>,
                response: Response<ContainerItemsResult>
            ) {
                response?.isSuccessful.let {
                    val listOfItems =
                        ContainerItemsResult(response.body()?.items ?: emptyList())
                    processData(listOfItems.items)
                }
            }

            private fun processData(items: List<ItemsResult>) {
                if (favorites.value!!.isNotEmpty()) {
                    for (i in items.indices) {
                        val item = items[i]
                        for (j in favorites.value!!.indices) {
                            if (item.id == favorites.value!![j].id) {
                                items[i].isFavorite = true
                                break
                            }
                        }
                    }
                }
                _search.value = items
            }
        })

    }


    val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.also {
                if (it.isNotEmpty()) {
                    search(it)
                }
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.also {
                if (it.isNotEmpty()) {
                    search(it)
                }
            }
            return false
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
