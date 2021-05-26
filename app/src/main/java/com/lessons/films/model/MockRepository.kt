package com.lessons.films.model

import java.util.*
import java.util.Collections.unmodifiableList

class MockRepository : Repository {
    var list: MutableList<Film> = mutableListOf(
            Film(1, "Любовь и голуби", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    true, 6.3, Date(1212121212121L)),
            Film(2, "Вий", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    false, 0.0, Date(1212121212121L)),
            Film(3, "Однажды на диком западе", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 3.6, Date(1212121212121L)),
            Film(4, "Не грози южному централу попивая сок у себя в квартале", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg",
                    true, 2.1, Date(1212121212121L)),
            Film(5, "Операция Ы", "https://cdn.pixabay.com/photo/2021/05/15/14/58/pied-kingfisher-6255945_960_720.jpg",
                    true, 9.0, Date(1212121212121L)),
            Film(6, "Кавказская пленница", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    false, 9.0, Date(1212121212121L)),
            Film(7, "Film7", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg",
                    false, 3.49, Date(1212121212121L)),
            Film(8, "Film8", "https://cdn.pixabay.com/photo/2020/04/23/16/45/eagle-5083248_960_720.jpg",
                    false, 9.0, Date(1212121212121L)),
            Film(9, "Film9", "https://cdn.pixabay.com/photo/2021/04/17/06/57/colour-6185159_960_720.jpg",
                    true, 1.3, Date(1212121212121L)),
            Film(10, "Film10", "https://cdn.pixabay.com/photo/2021/04/17/06/57/colour-6185159_960_720.jpg",
                    false, 3.29, Date(1212121212121L)),
            Film(11, "Film11", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 9.89, Date(1212121212121L)),
            Film(12, "Film12", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    false, 9.0, Date(1212121212121L))
    )
    var upcoming: MutableList<Film> = mutableListOf(
            Film(3, "Калина красная", "https://cdn.pixabay.com/photo/2020/03/25/09/30/belgium-4966646_960_720.jpg",
                    false, 0.0, Date(1212121212121L)),
            Film(1, "Фиксики", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg",
                    true, 0.0, Date(1212121212121L)),
            Film(2, "Фильм1", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    true, 0.0, Date(1212121212121L)),
            Film(2, "Фильм2", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg",
                    false, 0.0, Date(1212121212121L))
    )


    override fun getNowPlayingFilms() = unmodifiableList(list)

    override fun updateFilm(film: Film) {
        val ind = list.indexOf(list.find { it.id == film.id })
        list[ind] = film
    }

    override fun getUpcomingFilms() = unmodifiableList(upcoming)

}