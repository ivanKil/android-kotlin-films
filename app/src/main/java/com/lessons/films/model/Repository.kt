package com.lessons.films.model

import io.reactivex.Observable

interface Repository {
    fun getNowPlayingFilms(): Observable<List<Film>>

    fun updateFilm(film: Film)

    fun getUpcomingFilms(): Observable<List<Film>>

    fun getFilmDetails(filmId: Int): Observable<FilmDetail>

    fun searchFilms(filmName: String): Observable<List<Film>>
}