package com.jsv.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jsv.myapplication.model.Owner
import com.jsv.myapplication.model.SearchModel
import com.jsv.myapplication.service.ItemsResult


@Entity(tableName = "search_favorite")
data class DatabaseSearch constructor(

    @PrimaryKey()
    val id : Int? = 0,

    @ColumnInfo()
    val name: String? = "",

    @Embedded val owner: Owner? = null,

    @ColumnInfo()
    val description: String? = "",

    @ColumnInfo()
    val language: String? = "",

    @ColumnInfo()
    val isFavorite: Boolean = false
)


fun List<DatabaseSearch>.asDomainModel(): List<SearchModel> {
    return map{
        SearchModel(
            id = it.id,
            name = it.name,
            owner = it.owner,
            description = it.description,
            language = it.language)
    }
}


fun List<DatabaseSearch>.asItemDomainModel(): List<ItemsResult> {
    return map{
            ItemsResult(
                id = it.id,
                name = it.name,
                owner = it.owner,
                description = it.description,
                language = it.language,
                isFavorite = it.isFavorite)
    }
}

