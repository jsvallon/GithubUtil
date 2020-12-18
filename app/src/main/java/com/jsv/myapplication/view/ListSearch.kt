package com.jsv.myapplication.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.jsv.myapplication.R
import com.jsv.myapplication.database.GitHubDatabase
import com.jsv.myapplication.databinding.FragmentListSearchBinding
import com.jsv.myapplication.viewModel.ListSearchViewModel


class ListSearch : Fragment() {

    //Lazy initialize our ViewModel
    private val viewModel: ListSearchViewModel by lazy {

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        val dataSource = GitHubDatabase.getInstance(activity.application).gitHubDatabaseDao
        val viewModelFactory = ListSearchViewModelFactory(dataSource, activity.application)
        ViewModelProvider(this, viewModelFactory).get(ListSearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding  = FragmentListSearchBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.listSearch.adapter = SearchAdapter(SearchAdapter.OnClickListener {
            viewModel.insertFavorite(it)
        })

        viewModel.listFavorites.observe(viewLifecycleOwner, Observer {
            viewModel.doneFetchingFavorite(it)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId) {
          R.id.listSearch ->view!!.findNavController().navigate(R.id.listSearch)
          R.id.listSearchFavorites -> view!!.findNavController().navigate(R.id.listSearchFavorites)
       }
       return true
    }
}