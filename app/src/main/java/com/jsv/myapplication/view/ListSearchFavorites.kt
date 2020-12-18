package com.jsv.myapplication.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.jsv.myapplication.R
import com.jsv.myapplication.database.GitHubDatabase
import com.jsv.myapplication.databinding.FragmentListSearchFavoritesBinding
import com.jsv.myapplication.viewModel.ListSearchFavoritesViewModel


class ListSearchFavorites : Fragment() {


    //Lazy initialize our ViewModel
    private val viewModel: ListSearchFavoritesViewModel by lazy {

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        val dataSource = GitHubDatabase.getInstance(activity.application).gitHubDatabaseDao
        val viewModelFactory = ListSearchViewModelFactory(dataSource, activity.application)
        ViewModelProvider(this, viewModelFactory).get(ListSearchFavoritesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding  = FragmentListSearchFavoritesBinding.inflate(inflater)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.listFavorite.adapter = SearchAdapter(SearchAdapter.OnClickListener {

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