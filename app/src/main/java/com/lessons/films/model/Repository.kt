package com.lessons.films.model

import io.reactivex.Observable

interface Repository {
    fun getNowPlayingFilms(pageNum: Int): Observable<List<Film>>

    fun updateFilm(film: Film)

    fun getUpcomingFilms(): Observable<List<Film>>

    fun getFilmDetails(filmId: Int): Observable<FilmDetail>

    fun searchFilms(filmName: String, includeAdult: Boolean = false): Observable<List<Film>>

    fun getActorDetails(filmId: Int): Observable<ActorDetail>
}