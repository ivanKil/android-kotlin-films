package com.lessons.films.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lessons.films.model.AppState
import com.lessons.films.model.Film
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.Repository
import com.lessons.films.network.MovieRepository
import com.lessons.films.network.RetrofitServices

class MainViewModel : ViewModel() {
    val stateLiveData = MutableLiveData<AppState>()
    val stateLiveDataUpcoming = MutableLiveData<AppState>()
    val stateLiveDataFavorites = MutableLiveData<AppState>()
    val liveDataFilmDetail = MutableLiveData<FilmDetail>()
    private val repository: Repository = MovieRepository(RetrofitServices.create())
    val liveDataError = MutableLiveData<String>()

    fun requestNowPlayingFilms(filmName: String? = null) {
        stateLiveData.value = AppState.Loading
        repository.getNowPlayingFilms().subscribe(
                { stateLiveData.postValue(AppState.Success(it)) },
                { stateLiveData.value = AppState.Error(it) })
    }

    fun updateFilm(film: Film) {
        repository.updateFilm(film)
        repository.getNowPlayingFilms().subscribe({
            stateLiveData.value = AppState.Success(it)
        }, { stateLiveData.value = AppState.Error(it) })

        repository.getUpcomingFilms().subscribe({
            stateLiveDataUpcoming.value = AppState.Success(it)
        }, { stateLiveDataUpcoming.value = AppState.Error(it) })
    }

    fun requestUpcomingFilms() {
        stateLiveDataUpcoming.value = AppState.Loading
        repository.getUpcomingFilms().subscribe(
                { stateLiveDataUpcoming.postValue(AppState.Success(it)) },
                { stateLiveDataUpcoming.value = AppState.Error(it) })
    }

    fun requestFavoritesFilms() {
    }

    fun requestFilmDetail(filmId: Int) {
        repository.getFilmDetails(filmId).subscribe(
                { liveDataFilmDetail.postValue(it) },
                { liveDataError.value = it.message })
    }

    fun searchFilms(s: String) {
        repository.searchFilms(s).subscribe({
            stateLiveData.value = AppState.Success(it)
        }, { stateLiveData.value = AppState.Error(it) })
    }
}