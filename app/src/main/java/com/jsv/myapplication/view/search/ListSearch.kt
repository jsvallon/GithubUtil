package com.jsv.myapplication.view.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jsv.myapplication.R
import com.jsv.myapplication.database.GitHubDatabase
import com.jsv.myapplication.databinding.FragmentListSearchBinding
import com.jsv.myapplication.view.ListSearchViewModelFactory
import com.jsv.myapplication.view.SearchAdapter
import com.jsv.myapplication.viewModel.search.ListSearchViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class ListSearch : Fragment() {

    private lateinit var binding: FragmentListSearchBinding

    private lateinit var disposable: Disposable


    //Lazy initialize our ViewModel
    private val viewModel: ListSearchViewModel by lazy {

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        val dataSource = GitHubDatabase.getInstance(activity.application).gitHubDatabaseDao
        val viewModelFactory =
            ListSearchViewModelFactory(
                dataSource,
                activity.application
            )
        ViewModelProvider(this, viewModelFactory).get(ListSearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListSearchBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.listSearch.adapter =
            SearchAdapter(SearchAdapter.OnClickListener {
                viewModel.insertFavorite(it)
            })

        initSearchInputListener()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
      if (::disposable.isInitialized) {

          if (!disposable.isDisposed) {
            disposable.dispose()
          }
      }
      super.onDestroyView()
    }


    private fun initSearchInputListener() {
        val textChangeStream = createTextChangeObservable()
            .toFlowable(BackpressureStrategy.BUFFER)

        disposable = textChangeStream
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showProgress() }
            .observeOn(Schedulers.io())
            .map {
                doSearch(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                hideProgress()
                //showResult(it)
            }
    }


    private fun hideProgress() {
        binding.progressView.visibility = View.GONE
        viewModel.hideProgress()
    }

    private fun showProgress() {
        binding.progressView.visibility = View.VISIBLE
       viewModel.showProgress()
    }

    private fun createTextChangeObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->
            val onQueryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.also {
                        if (it.isNotEmpty()) {
                            emitter.onNext(it)
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.also {
                        if (it.isNotEmpty()) {
                            emitter.onNext(it)
                        }
                    }
                    return false
                }
             }

            binding.editSearch.setOnQueryTextListener(onQueryTextListener)

            emitter.setCancellable {
                binding.editSearch.setOnQueryTextListener(null)
                //queryEditText.removeTextChangedListener(textWatcher)
                //binding.editSearch.r
            }
        }

        return textChangeObservable
            .filter { it.length >= 2 }
            .debounce(1000, TimeUnit.MILLISECONDS)
    }


    private fun doSearch(input: String) {
        viewModel.search(input)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId) {
          R.id.listSearch ->{
              //view!!.findNavController().navigate(R.id.listSearch)
              findNavController().navigate(
                  R.id.listSearch
              )
          }
          R.id.listSearchFavorites -> {
              //view!!.findNavController().navigate(R.id.listSearchFavorites)
              findNavController().navigate(
                  R.id.listSearchFavorites
              )
          }
       }
       return true
    }
}