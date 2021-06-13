package com.lessons.films.model

import io.reactivex.Observable
import java.util.*
import java.util.Collections.unmodifiableList

class MockRepository : Repository {
    var list: MutableList<Film> = mutableListOf(
            Film(1, "Любовь и голуби", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    true, 6.3, Date(1212121212121L), listOf("Комедия", "Приключения", "Драма"), 117,
                    20400090),
            Film(2, "Вий", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    false, 0.0, Date(1212121212121L), listOf("Комедия", "Боевик"), 167,
                    20400090),
            Film(3, "Однажды на диком западе", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 3.6, Date(1212121212121L), listOf("Комедия", "Боевик", "Приключения", "Драма"), 117,
                    20400090),
            Film(4, "Не грози южному централу попивая сок у себя в квартале", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg",
                    true, 2.1, Date(1212121212121L), listOf("Комедия"), 117,
                    20400090),
            Film(5, "Операция Ы", "https://cdn.pixabay.com/photo/2021/05/15/14/58/pied-kingfisher-6255945_960_720.jpg",
                    true, 9.0, Date(1212121212121L), listOf("Комедия", "Приключения", "Драма"), 117,
                    20400090),
            Film(6, "Кавказская пленница", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    false, 9.0, Date(1212121212121L), listOf("Приключения", "Драма"), 101,
                    20400090),
            Film(7, "Film7", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg",
                    false, 3.49, Date(1212121212121L), listOf("Комедия", "Драма"), 117,
                    20400090),
            Film(8, "Film8", "https://cdn.pixabay.com/photo/2020/04/23/16/45/eagle-5083248_960_720.jpg",
                    false, 9.0, Date(1212121212121L), listOf("Комедия", "Боевик", "Приключения"), 166,
                    20400090),
            Film(9, "Film9", "https://cdn.pixabay.com/photo/2021/04/17/06/57/colour-6185159_960_720.jpg",
                    true, 1.3, Date(1212121212121L), listOf("Боевик", "Приключения", "Драма"), 117,
                    20400090),
            Film(10, "Film10", "https://cdn.pixabay.com/photo/2021/04/17/06/57/colour-6185159_960_720.jpg",
                    false, 3.29, Date(1212121212121L), listOf("Драма"), 117,
                    20400090),
            Film(11, "Film11", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 9.89, Date(1212121212121L), listOf("Комедия", "Боевик", "Приключения", "Драма"), 145,
                    20400090),
            Film(12, "Film12", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    false, 9.0, Date(1212121212121L), listOf("Комедия", "Боевик", "Приключения", "Драма"), 111,
                    20400090)
    )
    var upcoming: MutableList<Film> = mutableListOf(
            Film(13, "Калина красная", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 0.0, Date(1212121212121L), listOf("Комедия", "Приключения"), 117,
                    20400090),
            Film(14, "Фиксики", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    true, 0.0, Date(1212121212121L), listOf("Боевик", "Приключения", "Драма"), 117,
                    20400090),
            Film(15, "Фильм1", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    true, 0.0, Date(1212121212121L), listOf("Комедия", "Боевик", "Драма"), 137,
                    20400090),
            Film(16, "Фильм2", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    false, 0.0, Date(1212121212121L), listOf("Комедия", "Приключения", "Драма"), 117,
                    20400090)
    )


    override fun getNowPlayingFilms() = Observable.fromCallable { unmodifiableList(list) }

    override fun updateFilm(film: Film) {
        val ind = list.indexOf(list.find { it.id == film.id })
        if (ind != -1)
            list[ind] = film
        else {
            val ind = upcoming.indexOf(upcoming.find { it.id == film.id })
            upcoming[ind] = film
        }
    }

    override fun getUpcomingFilms() = Observable.fromCallable { unmodifiableList(upcoming) }
    override fun getFilmDetails(filmId: Int): Observable<FilmDetail> {
        TODO("Not yet implemented")
    }

    override fun searchFilms(filmName: String, includeAdult: Boolean): Observable<List<Film>> {
        TODO("Not yet implemented")
    }

}