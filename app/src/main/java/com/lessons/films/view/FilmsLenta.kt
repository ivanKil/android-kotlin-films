package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lessons.films.R
import com.lessons.films.databinding.FilmsLentaBinding
import com.lessons.films.model.AppState
import com.lessons.films.model.Film
import com.lessons.films.model.FilmDetail


open class FilmsLenta : Fragment() {
    private val filmsAdapter: FilmsAdapter by lazy { FilmsAdapter(Glide.with(this)) }
    var binding: FilmsLentaBinding? = null
    protected val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    protected var appState: AppState? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FilmsLentaBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    open fun setTitle() = binding!!.tvTitleList.setText(R.string.now_playing)

    open fun requestData() = viewModel.requestNowPlayingFilms()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObserve()
        appState ?: requestData()
    }

    open fun setObserve() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            appState = it
            renderData(it)
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
            override fun onFilmClicked(film: Film) =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.navigation_film_info,
                                    Bundle().apply { putParcelable(FilmInfoFragment.BUNDLE_EXTRA, FilmDetail.instanceFromfilm(film)) })


            override fun onFavoriteReverse(film: Film) =
                    viewModel.updateFilm(film.copy().apply { favorite = !favorite })
        }

        filmsList.adapter = filmsAdapter
    }

    protected fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                filmsAdapter.setData(appState.filmsData)
                binding!!.filmsProgress.visibility = View.GONE
                //Toast.makeText(requireContext(), resources.getString(R.string.ready), Toast.LENGTH_SHORT)
                //requireView().snackBarReady()
            }
            is AppState.Loading -> binding!!.filmsProgress.visibility = View.VISIBLE
            is AppState.Error -> {
                binding!!.filmsProgress.visibility = View.GONE
                Snackbar.make(requireView(), resources.getString(R.string.error) + ": " + appState.error.message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(resources.getString(R.string.reload_again)) { requestData() }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        filmsAdapter.removeListener()
        super.onDestroy()
    }
}
