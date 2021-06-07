package com.lessons.films.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.lessons.films.R
import com.lessons.films.databinding.FilmInfoFragmentBinding
import com.lessons.films.model.FilmDetail
import com.lessons.films.network.*
import com.lessons.films.snackBarError
import com.lessons.films.snackBarIntRes
import java.text.SimpleDateFormat

class FilmInfoFragment : Fragment() {
    private var binding: FilmInfoFragmentBinding? = null
    private val format = SimpleDateFormat("dd.MM.yyyy")
    private var film: FilmDetail? = null
    private val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra(DETAILS_LOAD_ERROR) != null) {
                requireView().snackBarError(intent.getStringExtra(DETAILS_LOAD_ERROR))
            } else if (intent.getParcelableExtra<FilmDetail>(DETAILS_LOAD_RESULT_EXTRA) != null) {
                setData(intent.getParcelableExtra(DETAILS_LOAD_RESULT_EXTRA))
            }
        }
    }


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
        val filmArg = arguments?.getParcelable<FilmDetail>(BUNDLE_EXTRA)
        filmArg?.let { fimArg ->
            setData(filmArg)
            binding!!.infoFavoriteImg.setOnClickListener {
                requireView().snackBarIntRes(R.string.click_heart)
            }

            context?.let {
                LocalBroadcastManager.getInstance(it)
                        .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))

                it.startService(Intent(it, DetailsService::class.java).apply {
                    putExtra(FILM_ID_EXTRA, filmArg.id)
                })
            }
        }
        viewModel.liveDataFilmDetail.observe(viewLifecycleOwner, ::setData)
        viewModel.liveDataError.observe(viewLifecycleOwner, view::snackBarError)
    }

    private fun setData(film: FilmDetail) {
        this.film = film
        binding?.apply {
            infoName.text = film.name
            infoBudget.text =
                    String.format(getResources().getString(R.string.budget),
                            if (film.budget == null || film.budget == 0) resources.getString(R.string.dont_know)
                            else film.budget.toString() + "$")
            infoGenge.text = film.genres?.map { it.name }?.joinToString()
            infoOverview.text = film.overview
            Glide.with(infoPoster).load(RetrofitServices.POSTER_BASE_URL + film.poster).centerCrop().into(infoPoster)
            infoReleseDate.text = String.format(getResources().getString(R.string.release_date), format.format(film.releaseDate))
            infoRunTime.text = String.format(getResources().getString(R.string.run_time), film.runTime.toString())
            infoVote.text = film.voteAverage.toString()
            infoOverview.text = film.overview
            infoFavoriteImg.setImageResource(if (film.favorite) R.drawable.favorite_fill_24 else R.drawable.favorites_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver) }
        binding = null
    }
}