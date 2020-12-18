package com.jsv.myapplication.model




import com.squareup.moshi.Json


data class SearchModel(
                       val id : Int?,
                       val name: String?,
                       val owner: Owner?,
                       val description: String?,
                       val language: String?)

data class Owner(
    val login: String,
    @Json(name="id") val ownerId: Long?,
    val avatar_url: String?)
