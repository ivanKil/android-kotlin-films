package com.lessons.films.network

import com.lessons.films.model.Film
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository(val apiService: RetrofitServices) : Repository {
    override fun getNowPlayingFilms(pageNum: Int): Observable<List<Film>> =
        apiService.getPlayingNow(pageNum).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMap { result ->
                Observable.fromCallable { result.results }
            }

    override fun updateFilm(film: Film) {
        //System.out.println("xc")
    }

    override fun getUpcomingFilms(): Observable<List<Film>> =
        apiService.getUpcoming(1/*FIXME страницы*/).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMap { result -> Observable.fromCallable { result.results } }

    override fun getFilmDetails(filmId: Int): Observable<FilmDetail> =
        apiService.getFilmDetails(filmId).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun searchFilms(filmName: String, includeAdult: Boolean): Observable<List<Film>> =
        apiService.searchFilms(filmName, includeAdult).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMap { result -> Observable.fromCallable { result.results } }

    override fun getActorDetails(actorId: Int) =
        apiService.getActorDetails(actorId).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}