package com.lessons.films.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lessons.films.model.AppState
import com.lessons.films.model.Film
import com.lessons.films.model.MockRepository
import com.lessons.films.model.Repository
import java.lang.Thread.sleep

class MainViewModel : ViewModel() {
    val stateLiveData = MutableLiveData<AppState>()
    val stateLiveDataUpcoming = MutableLiveData<AppState>()
    private val repository: Repository = MockRepository()
    val showInfoLiveData = MutableLiveData<Film>()
    fun requestNowPlayingFilms() {
        stateLiveData.value = AppState.Loading
        Thread {
            sleep(1000)
            when ((0..10).random()) {
                0 -> stateLiveData.postValue(AppState.Error(Throwable("Произошла ошибка")))
                else -> stateLiveData.postValue(AppState.Success(repository.getNowPlayingFilms()))
            }
        }.start()
    }

    fun updateFilm(film: Film) {
        repository.updateFilm(film)
        stateLiveData.value = AppState.Success(repository.getNowPlayingFilms())
    }

    fun showInfo(film: Film) {
        showInfoLiveData.value = film
    }

    fun requestUpcomingFilms() {
        stateLiveDataUpcoming.value = AppState.Loading
        Thread {
            sleep(1000)
            when ((0..1).random()) {
                0 -> stateLiveDataUpcoming.postValue(AppState.Success(repository.getUpcomingFilms()))
                1 -> stateLiveDataUpcoming.postValue(AppState.Error(Throwable("Произошла ошибка")))
            }
        }.start()
    }

    fun requestFavoritesFilms() {
        stateLiveData.value = AppState.Loading
        Thread {
            sleep(1000)
            stateLiveData.postValue(AppState.Success((repository.getUpcomingFilms() + repository.getNowPlayingFilms()).filter { it.favorite }))

        }.start()
    }
}