package com.lessons.films.network

import com.lessons.films.BuildConfig
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmsResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServices {
    @GET("movie/now_playing")
    fun getPlayingNow(
            @Query("page") page: Int,
            @Query("query") query: String? = null,
            @Query("api_key") api_key: String = API_KEY,
            @Query("language") language: String = LANG_RU,
    ): Observable<FilmsResponse>

    @GET("movie/upcoming")
    fun getUpcoming(
            @Query("page") page: Int,
            @Query("api_key") api_key: String = API_KEY,
            @Query("language") language: String = LANG_RU,
            @Query("region") region: String = REGION_RU): Observable<FilmsResponse>

    @GET("movie/{movie_id}")
    fun getFilmDetails(@Path(value = "movie_id", encoded = true) filmId: Int,
                       @Query("api_key") api_key: String = API_KEY,
                       @Query("language") language: String = LANG_RU): Observable<FilmDetail>

    @GET("search/movie")
    fun searchFilms(
            @Query("query") query: String? = null,
            @Query("api_key") api_key: String = API_KEY,
            @Query("language") language: String = LANG_RU,
    ): Observable<FilmsResponse>

    companion object Factory {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w92/"
        const val API_KEY: String = BuildConfig.FILMS_API_KEY
        const val LANG_RU = "ru-RU"
        const val REGION_RU = "RU"
        fun create(): RetrofitServices {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/").build()
            return retrofit.create(RetrofitServices::class.java)
        }
    }
}