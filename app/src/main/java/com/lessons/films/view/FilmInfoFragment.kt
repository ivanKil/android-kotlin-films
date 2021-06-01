package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lessons.films.R
import com.lessons.films.databinding.FilmInfoFragmentBinding
import com.lessons.films.model.AppState
import com.lessons.films.model.Film
import com.lessons.films.model.overviewTemp
import java.text.SimpleDateFormat

class FilmInfoFragment : Fragment() {
    private var binding: FilmInfoFragmentBinding? = null
    private val format = SimpleDateFormat("dd.MM.yyyy")
    private var film: Film? = null
    private val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

    companion object {
        const val BUNDLE_EXTRA: String = "extra_film"
        fun newInstance(bundle: Bundle) = FilmInfoFragment().apply { arguments = bundle }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FilmInfoFragmentBinding.inflate(inflater, container, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmArg = arguments?.getParcelable<Film>(BUNDLE_EXTRA)
        filmArg?.let { fimArg ->
            setData(filmArg)
            binding!!.infoFavoriteImg.setOnClickListener {
                viewModel.updateFilm(this.film!!.copy().apply { favorite = !favorite })
            }
            viewModel.stateLiveData.observe(viewLifecycleOwner) { appState ->
                when (appState) {
                    is AppState.Success ->
                        appState.filmsData.find { it.id == filmArg.id }?.let { setData(it) }
                }
            }
        }
    }

    fun setData(film: Film) {
        this.film = film
        binding?.apply {
            infoName.text = film.name
            infoBudget.text = String.format(getResources().getString(R.string.budget), film.budget.toString())
            infoGenge.text = film.genres?.joinToString()
            infoOverview.text = film.overview
            Glide.with(infoPoster).load(film.poster).centerCrop().into(infoPoster)
            infoReleseDate.text = String.format(getResources().getString(R.string.release_date), format.format(film.releaseDate))
            infoRunTime.text = String.format(getResources().getString(R.string.run_time), film.runTime.toString())
            infoVote.text = film.voteAverage.toString()
            infoOverview.text = film.overviewTemp
            infoFavoriteImg.setImageResource(if (film.favorite) R.drawable.favorite_fill_24 else R.drawable.favorites_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}