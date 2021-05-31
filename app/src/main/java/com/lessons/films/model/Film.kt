package com.lessons.films.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Film(val id: Int, val name: String, val poster: String?, var favorite: Boolean, val voteAverage: Double = 0.0, val releaseDate: Date,
                val genres: List<String>?, val runTime: Int?, val budget: Int?, val overview: String? = null) : Parcelable

//заглушка, потом удалю
val Film.overviewTemp: String
    get() = "Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решётки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, обладающий живым умом и доброй душой, находит подход как к заключённым, так и к охранникам, добиваясь их особого к себе расположения."