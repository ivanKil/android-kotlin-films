package com.lessons.films.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lessons.films.R
import com.lessons.films.databinding.FilmInfoFragmentBinding
import com.lessons.films.model.FilmDetail
import com.lessons.films.network.*
import com.lessons.films.snackBarError
import com.lessons.films.view.map.BUNDLE_ACTOR_ID
import java.text.SimpleDateFormat

class FilmInfoFragment : Fragment() {
    private var binding: FilmInfoFragmentBinding? = null
    private val format = SimpleDateFormat("dd.MM.yyyy")
    private val formatDateTime = SimpleDateFormat("dd.MM.yyyy HH:mm")
    private var film: FilmDetail? = null
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra(DETAILS_LOAD_ERROR) != null) {
                requireView().snackBarError(intent.getStringExtra(DETAILS_LOAD_ERROR))
            } else if (intent.getParcelableExtra<FilmDetail>(DETAILS_LOAD_RESULT_EXTRA) != null) {
                setData(intent.getParcelableExtra(DETAILS_LOAD_RESULT_EXTRA))
                viewModel.saveFilmDetailToDB(film!!)
            }
        }
    }


    companion object {
        const val BUNDLE_EXTRA: String = "extra_film"
        //fun newInstance(bundle: Bundle) = FilmInfoFragment().apply { arguments = bundle }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilmInfoFragmentBinding.inflate(inflater, container, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmArg = arguments?.getParcelable<FilmDetail>(BUNDLE_EXTRA)
        filmArg?.let { fimArg ->
            setData(filmArg)
            binding!!.infoFavoriteImg.setOnClickListener {
                viewModel.setFavorite(filmArg.id, !film!!.favorite)
                setFavoritePic(false)
            }
            if (filmArg.inDb)
                viewModel.requestFilmDetailFromDb(filmArg.id)
            context?.let {
                LocalBroadcastManager.getInstance(it)
                    .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))

                it.startService(Intent(it, DetailsService::class.java).apply {
                    putExtra(FILM_ID_EXTRA, filmArg.id)
                })
            }
            //setFavoritePic(filmArg.favorite)
            viewModel.requestIsFavoriteFromDb(filmArg.id)
        }
        viewModel.liveDataFilmDetail.observe(viewLifecycleOwner, ::setData)
        viewModel.liveDataError.observe(viewLifecycleOwner, view::snackBarError)
        viewModel.liveDataFavorite.observe(viewLifecycleOwner, ::setFavoritePic)
    }

    private fun setFavoritePic(isFavorite: Boolean) {
        film!!.favorite = isFavorite
        binding?.apply {
            infoFavoriteImg.setImageResource(if (isFavorite) R.drawable.favorite_fill_24 else R.drawable.favorites_24)
        }
    }

    private fun setData(film: FilmDetail) {
        this.film = film.apply { favorite = this@FilmInfoFragment.film?.favorite ?: false }
        binding?.apply {
            infoName.text = film.name
            infoBudget.text =
                String.format(
                    getResources().getString(R.string.budget),
                    if (film.budget == null || film.budget == 0) resources.getString(R.string.dont_know)
                    else film.budget.toString() + "$"
                )
            infoGenge.text = film.genres?.map { it.name }?.joinToString()
            infoOverview.text = film.overview
            //Glide.with(infoPoster).load(RetrofitServices.POSTER_BASE_URL + film.poster).centerCrop().into(infoPoster)
            infoPoster.load(RetrofitServices.POSTER_BASE_URL + film.poster) {
                crossfade(2000)
                //scale(Scale.FILL)
                transformations(RoundedCornersTransformation(5.0F))
            }
            infoReleseDate.text = String.format(
                getResources().getString(R.string.release_date),
                format.format(film.releaseDate)
            )
            infoRunTime.text =
                String.format(getResources().getString(R.string.run_time), film.runTime.toString())
            infoVote.text = film.voteAverage.toString()
            infoOverview.text = film.overview
            //infoFavoriteImg.setImageResource(if (film.favorite) R.drawable.favorite_fill_24 else R.drawable.favorites_24)
            infoNote.setOnClickListener {
                NoteFragment.newInstance(film).show(childFragmentManager, "EDIT_TAG")
            }
            infoSaveDate.visibility = if (film.saveDate == null) View.GONE else View.VISIBLE
            film.saveDate?.let {
                infoSaveDate.text =
                    String.format(
                        getResources().getString(R.string.showed),
                        formatDateTime.format(film.saveDate)
                    )
            }
            film.credits?.cast?.subList(
                0,
                if (film.credits!!.cast!!.size > 5) 5 else film.credits!!.cast!!.size
            )?.forEach {
                infoFlexActors.addView(TextView(requireContext()).apply {
                    text = it.name
                    val actorId = it.id
                    setPadding(
                        0,
                        resources.getInteger(R.integer.actor_padding),
                        resources.getInteger(R.integer.actor_padding),
                        resources.getInteger(R.integer.actor_padding)
                    )
                    paintFlags = getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG
                    setOnClickListener {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.navigation_map,
                                Bundle().apply {
                                    putInt(BUNDLE_ACTOR_ID, actorId)
                                })
                    }
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        viewModel.liveDataFilmDetail.removeObservers(this)
        binding = null
    }
}