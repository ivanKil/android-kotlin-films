package com.lessons.films.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Film(
    val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("poster_path") val poster: String?,
    var favorite: Boolean,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("release_date") val releaseDate: Date,
    @SerializedName("genre_ids") val genres: List<String>?,
    val runTime: Int?,
    val budget: Int?,
    val overview: String? = null,
    @Transient val inDb: Boolean = false
) : Parcelable {
    companion object {
        fun instanceFromFilmDetail(film: FilmDetail) = with(film) {
            Film(
                id, name, poster, favorite, voteAverage, releaseDate, null, runTime,
                budget, overview, true
            )
        }
    }
}

@Parcelize
data class FilmsResponse(
    val page: Int?,
    val results: List<Film>?,
    val total_pages: Int?,
    val total_results: Int?
) : Parcelable


@Parcelize
data class Genre(val id: Int, val name: String) : Parcelable

@Entity
@Parcelize
data class FilmDetail(
    @PrimaryKey val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("poster_path") val poster: String?,
    var favorite: Boolean = false,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("release_date") val releaseDate: Date,
    @SerializedName("genres") val genres: List<Genre>?,
    @SerializedName("runtime") val runTime: Int?,
    val budget: Int?,
    val overview: String? = null,
    var saveDate: Date?
) : Parcelable {

    constructor(
        id: Int,
        name: String,
        poster: String?,
        favorite: Boolean = false,
        voteAverage: Double = 0.0,
        releaseDate: Date,
        genres: List<Genre>?,
        runTime: Int?,
        budget: Int?,
        overview: String? = null,
        aveDate: Date?, credits: Credit
    ) : this(
        id, name, poster, favorite, voteAverage,
        releaseDate, genres, runTime, budget, overview, aveDate
    ) {
        this.credits = credits
    }

    @Ignore
    var inDb: Boolean = false

    @Ignore
    var credits: Credit? = null

    companion object {
        fun instanceFromfilm(film: Film) = with(film) {
            var v = FilmDetail(
                id, name, poster, favorite, voteAverage, releaseDate, null, runTime,
                budget, overview, null
            )
            v.inDb = film.inDb
            v
        }
    }
}

@Parcelize
data class Credit(val cast: List<Cast>? = null) : Parcelable

@Parcelize
data class Cast(val id: Int, val name: String) : Parcelable

@Parcelize
data class ActorDetail(val id: Int, @SerializedName("place_of_birth") val placeOfBirth: String?) :
    Parcelable

@Entity
@Parcelize
data class FilmNote(@PrimaryKey val filmId: Int, val text: String) : Parcelable

@Entity
@Parcelize
data class FavoriteID(@PrimaryKey val filmId: Int) : Parcelable