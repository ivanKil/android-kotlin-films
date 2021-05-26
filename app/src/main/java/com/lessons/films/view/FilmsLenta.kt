package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lessons.films.R
import com.lessons.films.databinding.FilmsLentaBinding
import com.lessons.films.model.AppState
import com.lessons.films.model.Film


open class FilmsLenta : Fragment() {
    private val filmsAdapter = FilmsAdapter()
    var binding: FilmsLentaBinding? = null

    companion object {
        fun newInstance() = FilmsLenta()
    }

    lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FilmsLentaBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    open fun setTitle() {
        binding!!.tvTitleList.setText(R.string.now_playing)
    }

    open fun requestData() = viewModel.requestNowPlayingFilms()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.stateLiveData.observe(viewLifecycleOwner) { renderData(it) }
        if (savedInstanceState == null) {
            requestData()
        }
    }

    open fun getRecyclerViewLayout(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width / 3
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT
                return true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filmsList: RecyclerView = view.findViewById(R.id.list_layot)
        setTitle()
        filmsList.layoutManager = getRecyclerViewLayout()
        filmsAdapter.clickListener = object : OnFilmClicked {
            override fun onFilmClicked(film: Film) {
                Toast.makeText(requireContext(), R.string.temp, Toast.LENGTH_SHORT).show()
            }

            override fun onFavoriteReverse(film: Film) {
                val f = film.copy()
                f.favorite = !film.favorite
                viewModel.updateFilm(f)
            }
        }
        filmsList.adapter = filmsAdapter
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                filmsAdapter.setData(appState.filmsData)
                binding!!.filmsProgress.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.ready), Toast.LENGTH_SHORT)
            }
            is AppState.Loading -> {
                binding!!.filmsProgress.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding!!.filmsProgress.visibility = View.GONE
                Snackbar.make(requireView(), resources.getString(R.string.error) + ": " + appState.error.message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(resources.getString(R.string.reload_again)) { requestData() }
                        .show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}