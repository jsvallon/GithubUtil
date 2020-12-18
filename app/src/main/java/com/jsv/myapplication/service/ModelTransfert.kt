package com.jsv.myapplication.service


import com.jsv.myapplication.database.DatabaseSearch
import com.jsv.myapplication.model.Owner


data class ContainerItemsResult(val items: List<ItemsResult>)


data class ItemsResult (
    val id : Int?,
    val name: String?,
    val owner: Owner?,
    val description: String?,
    val language: String?,
    var isFavorite: Boolean = false) {

    val capitalizeFirstName: String?
    get() = name?.capitalize()
}




fun ItemsResult.asDomainModel(itemsResult: ItemsResult): DatabaseSearch {
    return DatabaseSearch(
         itemsResult.id,
         itemsResult.name,
         itemsResult.owner,
         itemsResult.description,
         itemsResult.language,
         itemsResult.isFavorite
    )
}

