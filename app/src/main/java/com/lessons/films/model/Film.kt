package com.lessons.films.model

import java.util.*

data class Film(val id: Int, val name: String, val poster: String?, var favorite: Boolean, val voteAverage: Double = 0.0, val releaseDate: Date)