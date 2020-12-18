package com.jsv.myapplication.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = " https://api.github.com/"

interface IGithubApi {
    @GET("search/repositories")
    //fun search(@Header("Authorization") authorization: String , @Query("q") query: String) : Call<ContainerItemsResult>
    fun search(@Query("q") query: String) : Call<ContainerItemsResult>
}

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

object GitHubApi {
    val serviceRetrofit : IGithubApi by lazy {
        retrofit.create(IGithubApi::class.java)
    }
}