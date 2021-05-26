package com.lessons.films.model

interface Repository {
    fun getNowPlayingFilms(): List<Film>

    fun updateFilm(film: Film)
    
    fun getUpcomingFilms(): List<Film>
}