package com.jsv.myapplication.view



import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jsv.myapplication.R
import com.jsv.myapplication.service.ItemsResult
import com.makeramen.roundedimageview.RoundedImageView



@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ItemsResult>?) {
    var adapter = recyclerView.adapter as SearchAdapter
    val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
    itemDecoration.setDrawable(getDrawable(recyclerView.context,R.drawable.divider)!!)
    recyclerView.addItemDecoration(itemDecoration)
    adapter.submitList(data)
}



@BindingAdapter("listFavoriteData")
fun bindRecyclerViewFavorite(recyclerView: RecyclerView, data: List<ItemsResult>?) {
    var adapter = recyclerView.adapter as SearchAdapter
    recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    adapter.submitList(data)
}


@BindingAdapter("ownerImageView")
fun loadOwnerProfile(img: RoundedImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(img.context)
            .load(imgUri)
            .apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_img))
            .into(img)
    }
}

@BindingAdapter("isFavorite")
fun isFavorite(img: ImageView, isFavorite: Boolean?) {
    isFavorite?.let {favorite->
        when(favorite) {
            true -> img.setImageResource(R.drawable.ic_favorite_fill)
            false -> img.setImageResource(R.drawable.ic_favorite_empty)
        }
    }
}



@BindingAdapter("query")
fun setQuery(
    searchView: SearchView,
    queryText: String?
) {
    searchView.setQuery(queryText, false)
}

@BindingAdapter("queryTextListener")
fun setOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener?) {
    searchView.setOnQueryTextListener(listener)
}



