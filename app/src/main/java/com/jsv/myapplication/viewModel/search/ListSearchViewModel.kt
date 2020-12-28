package com.jsv.myapplication.viewModel.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsv.myapplication.database.GitHubDatabaseDao
import com.jsv.myapplication.database.asItemDomainModel
import com.jsv.myapplication.repository.SearchRepository
import com.jsv.myapplication.service.ContainerItemsResult
import com.jsv.myapplication.service.GitHubApi
import com.jsv.myapplication.service.ItemsResult
import com.jsv.myapplication.service.asDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class GitHubApiStatus { LOADING, ERROR, DONE}

class ListSearchViewModel(val database: GitHubDatabaseDao, application: Application) :
    AndroidViewModel(application) {


    //Favorite functions
    private val disposable = CompositeDisposable()


    private val searchRepository = SearchRepository(database)

    private val _search = MutableLiveData<List<ItemsResult>>()
    val search: LiveData<List<ItemsResult>>
        get() = _search

    private val _favorites = MutableLiveData<List<ItemsResult>>()
    private val favorites: LiveData<List<ItemsResult>>
        get() = _favorites


    //For ProgressBar
    private val _loading = MutableLiveData<GitHubApiStatus>()
    val loading: LiveData<GitHubApiStatus>
        get() = _loading


    init {
        getListFavorites()
    }


    fun insertFavorite(itemResult: ItemsResult) {
        val databaseEntity = itemResult.asDomainModel(itemResult)
        searchRepository.insertFavorite(databaseEntity)
    }

    private fun getListFavorites() {
        disposable.add(
            searchRepository.getListFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {

                }
                .subscribe {
                    doneFetchingFavorite(it.asItemDomainModel())
                }
        )
    }


    private fun doneFetchingFavorite(itemsResult: List<ItemsResult>) {
        _favorites.value = itemsResult
    }

    fun showProgress() {
        _loading.value = GitHubApiStatus.LOADING
    }

    fun hideProgress() {
        _loading.value = GitHubApiStatus.DONE
    }


    fun search(search: String) {
        GitHubApi.serviceRetrofit.search(search)
        .enqueue(object : Callback<ContainerItemsResult> {
            override fun onFailure(call: Call<ContainerItemsResult>, t: Throwable) {
                _loading.value = GitHubApiStatus.ERROR
            }

            override fun onResponse(
                call: Call<ContainerItemsResult>,
                response: Response<ContainerItemsResult>
            ) {
                response?.isSuccessful.let {
                    val listOfItems = ContainerItemsResult(response.body()?.items ?: emptyList())
                    processData(listOfItems.items)
                }
            }
        })
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
