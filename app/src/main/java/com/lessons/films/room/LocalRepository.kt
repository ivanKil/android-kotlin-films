package com.lessons.films.room

import com.lessons.films.model.FavoriteID
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmNote
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

interface LocalRepository {
    fun getAllHistory(): Observable<List<FilmDetail>>
    fun saveEntity(filmDetail: FilmDetail)
    fun getFilmDetailById(id: Int): Flowable<FilmDetail>
    fun getFilmNoteById(id: Int): Flowable<FilmNote>
    fun saveEntity(filmNote: FilmNote): Completable
    fun setFavorite(filmID: FavoriteID, isFavorite: Boolean): Completable
    fun getFavorites(): Observable<List<FilmDetail>>
    fun getFavoriteById(filmId: Int): Flowable<Boolean>
}