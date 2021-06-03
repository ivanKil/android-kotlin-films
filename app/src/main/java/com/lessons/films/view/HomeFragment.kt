package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lessons.films.R

class HomeFragment : Fragment() {
    protected val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.films_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = initTopMenu(view)

    private fun initTopMenu(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val menu = toolbar.menu
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.searchFilms(s)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_sort_by_name -> {
                    //viewModel.sortByName()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_sort_by_date -> {
                    //viewModel.sortByDate()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }
}