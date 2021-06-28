package com.lessons.films.room

import com.lessons.films.model.FavoriteID
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmNote
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalRepositoryImpl(private val localDataSource: HistoryDao) :
        LocalRepository {

    override fun getAllHistory(): Observable<List<FilmDetail>> {
        return localDataSource.all().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveEntity(film: FilmDetail) {
        Completable.fromCallable {
            localDataSource.insertFilmDetail(film)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun getFilmDetailById(id: Int): Flowable<FilmDetail> {
        return localDataSource.getFilmById(id).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFilmNoteById(id: Int): Flowable<FilmNote> {
        return localDataSource.getFilmNoteById(id).observeOn(AndroidSchedulers.mainThread())
    }

    override fun setFavorite(filmID: FavoriteID, isFavorite: Boolean) =
            Completable.fromCallable {
                if (!isFavorite)
                    localDataSource.delete(filmID)
                else
                    localDataSource.setFavorite(filmID)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun getFavorites(): Observable<List<FilmDetail>> {
        return localDataSource.getFavoriteFilms().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteById(filmId: Int): Flowable<Boolean> {
        return localDataSource.getFavoriteById(filmId)
                .flatMap {
                    Flowable.fromCallable {
                        it != null
                    }
                }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveEntity(filmNote: FilmNote) =
            Completable.fromCallable {
                localDataSource.insertFilmNote(filmNote)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}