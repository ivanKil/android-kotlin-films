package com.lessons.films.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.lessons.films.R
import com.lessons.films.hideKeyboard

class HomeFragment : Fragment() {
    protected val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //setHasOptionsMenu(true)
        return inflater.inflate(R.layout.films_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = initTopMenu(view)

    private fun initTopMenu(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val menu = toolbar.menu
//        activity?.let {
//            menu.getItem(3).isChecked =
//                    it.getPreferences(Context.MODE_PRIVATE).getBoolean(HAS_ADULT, false)
//        }
        menu.getItem(3).setChecked(viewModel.hasAdult())
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.searchFilms(s)
                view.hideKeyboard()
                searchView.clearFocus()

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.navigation_films_searched)
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
                R.id.action_adult -> {
                    val hasAdult = !item.isChecked
                    item.setChecked(hasAdult)
                    //activity?.getPreferences(Context.MODE_PRIVATE)!!.edit().putBoolean(HAS_ADULT, hasAdult).apply()
                    viewModel.saveAdult(hasAdult)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        searchView.clearFocus()
        view.hideKeyboard()
    }

    override fun onResume() {
        view?.hideKeyboard()
        //menu.getItem(3).isChecked == viewModel.hasAdult()
        super.onResume()
    }
}