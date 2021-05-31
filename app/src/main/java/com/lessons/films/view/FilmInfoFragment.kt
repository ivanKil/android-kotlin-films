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

    companion object {
        const val BUNDLE_EXTRA: String = "extra_film"

        fun newInstance(bundle: Bundle): FilmInfoFragment {
            val fragment = FilmInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FilmInfoFragmentBinding.inflate(inflater, container, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val film = arguments?.getParcelable<Film>(BUNDLE_EXTRA)
        if (film != null) {
            setData(film)
            binding!!.infoFavoriteImg.setOnClickListener {
                val f = this.film!!.copy()
                f.favorite = !f.favorite
                viewModel.updateFilm(f)
            }
            viewModel.stateLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is AppState.Success -> {
                        var fUpdated = it.filmsData.find { it.id == film.id }
                        if (fUpdated != null)
                            setData(fUpdated)
                    }
                }
            }
        }
    }

    fun setData(film: Film) {
        this.film = film
        binding!!.infoName.text = film.name
        binding!!.infoBudget.text =
                String.format(getResources().getString(R.string.budget), film.budget.toString())
        binding!!.infoGenge.text = film.genres?.joinToString()
        binding!!.infoOverview.text = film.overview
        Glide.with(binding!!.infoPoster)
                .load(film.poster).centerCrop()
                .into(binding!!.infoPoster)
        binding!!.infoReleseDate.text = String.format(getResources().getString(R.string.release_date), format.format(film.releaseDate))
        binding!!.infoRunTime.text =
                String.format(getResources().getString(R.string.run_time), film.runTime.toString())
        binding!!.infoVote.text = film.voteAverage.toString()
        binding!!.infoOverview.text = film.overviewTemp
        binding!!.infoFavoriteImg.setImageResource(if (film.favorite) R.drawable.favorite_fill_24 else R.drawable.favorites_24)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}