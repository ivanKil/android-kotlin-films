package com.lessons.films.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lessons.films.App.Companion.getHistoryDao
import com.lessons.films.model.*
import com.lessons.films.network.MovieRepository
import com.lessons.films.network.RetrofitServices
import com.lessons.films.room.LocalRepository
import com.lessons.films.room.LocalRepositoryImpl
import java.util.*

private const val HAS_ADULT = "HAS_ADULT"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val stateLiveData = MutableLiveData<AppState>()
    val stateLiveDataUpcoming = MutableLiveData<AppState>()
    val stateLiveDataFavorites = MutableLiveData<AppState>()
    val liveDataFilmDetail = MutableLiveData<FilmDetail>()
    val liveDataSearch = MutableLiveData<AppState>()
    val liveDataFilmsHistory = MutableLiveData<AppState>()
    val liveDataFilmNote = MutableLiveData<FilmNote>()
    val liveDataFavorite = MutableLiveData<Boolean>()

    private val repository: Repository = MovieRepository(RetrofitServices.create())
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())


    val liveDataError = MutableLiveData<String>()
    val pref = getApplication<Application>().applicationContext.getSharedPreferences("qwer", Context.MODE_PRIVATE)

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
        stateLiveDataFavorites.value = AppState.Loading
        historyRepository.getFavorites().subscribe(
                {
                    stateLiveDataFavorites.postValue(
                            AppState.Success(it.map {
                                Film.instanceFromFilmDetail(it)
                            }))
                },
                {
                    it.printStackTrace()
                    stateLiveDataFavorites.value = AppState.Error(it)
                })
    }

    fun requestIsFavoriteById(id: Int) = historyRepository.getFavoriteById(id)


    fun requestFilmDetail(filmId: Int) {
        repository.getFilmDetails(filmId).subscribe(
                { liveDataFilmDetail.postValue(it) },
                { liveDataError.value = it.message })
    }


    fun searchFilms(s: String) {
        if (s == "")
            liveDataSearch.value = AppState.Success(listOf())
        else
            repository.searchFilms(s, pref.getBoolean(HAS_ADULT, false)).subscribe({
                liveDataSearch.value = AppState.Success(it)
            }, { liveDataSearch.value = AppState.Error(it) })
    }

    fun hasAdult() = pref.getBoolean(HAS_ADULT, false)
    fun saveAdult(hasAdult: Boolean) = pref.edit().putBoolean(HAS_ADULT, hasAdult).apply()

    fun saveNote(id: Int, text: String) =
            historyRepository.saveEntity(FilmNote(id, text))


    fun saveFilmDetailToDB(filmDetail: FilmDetail) {
        if (filmDetail.saveDate == null)
            filmDetail.saveDate = Date()
        historyRepository.saveEntity(filmDetail)
    }


    fun requestFilmsHistory() {
        //liveDataFilmsHistory.value = AppState.Success(historyRepository.getAllHistory().map { Film.instanceFromFilmDetail(it) })
        historyRepository.getAllHistory().subscribe({
            liveDataFilmsHistory.value = AppState.Success(it.map { Film.instanceFromFilmDetail(it) })
        }, { liveDataError.value = it.message })
    }

    fun requestFilmDetailFromDb(id: Int) {
        historyRepository.getFilmDetailById(id)
                .subscribe({ liveDataFilmDetail.value = it },
                        { liveDataError.value = it.message })
    }

    fun requestFilmNoteFromDb(id: Int) {
        historyRepository.getFilmNoteById(id)
                .subscribe({
                    liveDataFilmNote.value = it
                }, { liveDataError.value = it.message })
    }

    fun setFavorite(id: Int, isFavorite: Boolean) =
            historyRepository.setFavorite(FavoriteID(id), isFavorite)
                    .subscribe({ requestIsFavoriteFromDb(id) },
                            { liveDataError.value = it.message })

    fun requestIsFavoriteFromDb(id: Int) {
        historyRepository.getFavoriteById(id)
                .subscribe({
                    liveDataFavorite.value = it
                }, {
                    liveDataError.value = it.message
                })
    }
}