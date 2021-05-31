package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lessons.films.R

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.films_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTopMenu(view)
    }

    private fun initTopMenu(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val menu = toolbar.menu
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                //viewModel.filterByName(s)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                //viewModel.filterByName(s)
                return true
            }
        })
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_sort_by_name) {
                //viewModel.sortByName()
                return@setOnMenuItemClickListener true
            }
            if (item.itemId == R.id.action_sort_by_date) {
                //viewModel.sortByDate()
                return@setOnMenuItemClickListener true
            }
            false
        }
    }

}