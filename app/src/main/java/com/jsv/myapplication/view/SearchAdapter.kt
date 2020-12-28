package com.jsv.myapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jsv.myapplication.databinding.SingleSearchItemBinding
import com.jsv.myapplication.service.ItemsResult
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.single_search_item.view.*

class SearchAdapter (private val onclickListener: OnClickListener): ListAdapter<ItemsResult, SearchAdapter.SearchModelViewHolder>(DiffCallBack) {

    var compositeDisposable = CompositeDisposable()

    class OnClickListener(val onclickListener: (itemsResult: ItemsResult) ->Unit) {
        fun saveAsFavorite(itemsResult: ItemsResult) = onclickListener(itemsResult)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<ItemsResult>() {
        override fun areItemsTheSame(oldItem: ItemsResult, newItem: ItemsResult): Boolean {
             return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ItemsResult, newItem: ItemsResult): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class SearchModelViewHolder(private var binding: SingleSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(itemsResult: ItemsResult) {
           binding.itemsresult = itemsResult
           binding.executePendingBindings()
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchModelViewHolder {
        return SearchModelViewHolder(SingleSearchItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SearchModelViewHolder, position: Int) {
        val itemsResult = getItem(position)
//        holder.itemView.setOnClickListener {
//            println("onBindViewHolder setOnClickListener")
//
//            //onclickListener.saveAsFavorite(it)
//        }

        compositeDisposable.add(Maybe.create<ItemsResult> { emitter ->
            emitter.setCancellable {
                holder.itemView.favorite.setOnClickListener(null)
            }
            holder.itemView.favorite.setOnClickListener {
                println("onBindViewHolder setOnClickListener")
                emitter.onSuccess(itemsResult)
            }
        }.toFlowable().onBackpressureLatest()
            .observeOn(Schedulers.io())
            .map {
                it.isFavorite = !it.isFavorite
                println("onBindViewHolder answer setOnClickListener ${it.isFavorite} ")

                onclickListener.saveAsFavorite(it)
                itemsResult.isFavorite
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isFavorite(holder.itemView.favorite,it)
            }
        )

        holder.bind(itemsResult)

    }

    //Still don't know if I have to put it.
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }
}
