package com.lessons.films.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.lessons.films.model.FavoriteID
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmNote
import com.lessons.films.model.Genre
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(hobbies: List<Genre>): String? {
        return hobbies.map { "${it.id},${it.name}" }.joinToString("|")
    }

    @TypeConverter
    fun toStringList(data: String?): List<Genre>? {
        return data?.split("|")?.map {
            val arr = it.split(",")
            Genre(arr[0].toInt(), arr[1])
        } ?: listOf()
    }
}

@Database(entities = [FilmDetail::class, FilmNote::class, FavoriteID::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}