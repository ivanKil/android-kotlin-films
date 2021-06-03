package com.lessons.films.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Film(val id: Int, @SerializedName("title") val name: String,
                @SerializedName("poster_path") val poster: String?, var favorite: Boolean,
                @SerializedName("vote_average") val voteAverage: Double = 0.0,
                @SerializedName("release_date") val releaseDate: Date,
                @SerializedName("genre_ids") val genres: List<String>?,
                val runTime: Int?, val budget: Int?, val overview: String? = null) : Parcelable

@Parcelize
data class FilmsResponse(val page: Int?, val results: List<Film>?, val total_pages: Int?, val total_results: Int?) : Parcelable


@Parcelize
data class Genre(val id: Int, val name: String) : Parcelable

@Parcelize
data class FilmDetail(val id: Int, @SerializedName("title") val name: String,
                      @SerializedName("poster_path") val poster: String?, var favorite: Boolean,
                      @SerializedName("vote_average") val voteAverage: Double = 0.0,
                      @SerializedName("release_date") val releaseDate: Date,
                      @SerializedName("genres") val genres: List<Genre>?,
                      @SerializedName("runtime") val runTime: Int?,
                      val budget: Int?, val overview: String? = null) : Parcelable {
    companion object {
        fun instanceFromfilm(film: Film) = with(film) {
            FilmDetail(id, name, poster, favorite, voteAverage, releaseDate, null, runTime,
                    budget, overview)
        }
    }


}