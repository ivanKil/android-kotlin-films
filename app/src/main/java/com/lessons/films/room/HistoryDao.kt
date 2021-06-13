package com.lessons.films.room

import androidx.room.*
import com.lessons.films.model.FavoriteID
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmNote
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface HistoryDao {

    @Query("SELECT * FROM FilmDetail")
    fun all(): Observable<List<FilmDetail>>

    @Query("SELECT fd.* FROM FavoriteID f left join FilmDetail fd on f.filmId=fd.id ")
    fun getFavoriteFilms(): Observable<List<FilmDetail>>

    @Query("SELECT * FROM FilmDetail WHERE name LIKE :name")
    fun getDataByWord(name: String): List<FilmDetail>

    @Query("SELECT * FROM FilmDetail WHERE id = :id")
    fun getFilmById(id: Int): Flowable<FilmDetail>

    @Query("SELECT * FROM FilmNote WHERE filmId = :id")
    fun getFilmNoteById(id: Int): Flowable<FilmNote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmDetail(entity: FilmDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmNote(entity: FilmNote)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setFavorite(filmId: FavoriteID)

    @Update
    fun update(entity: FilmDetail)

    @Delete
    fun delete(entity: FilmDetail)

    @Delete
    fun delete(entity: FavoriteID)

    @Query("SELECT * FROM FavoriteID WHERE filmId = :id")
    fun getFavoriteById(id: Int): Flowable<FavoriteID>
}