package com.lessons.films.network

import com.lessons.films.BuildConfig
import com.lessons.films.model.FilmDetail
import com.lessons.films.model.FilmsResponse
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException


interface RetrofitServices {
    @GET("movie/now_playing")
    fun getPlayingNow(
            @Query("page") page: Int,
            @Query("query") query: String? = null,
    ): Observable<FilmsResponse>

    @GET("movie/upcoming")
    fun getUpcoming(
            @Query("page") page: Int,
            @Query("region") region: String = REGION_RU): Observable<FilmsResponse>

    @GET("movie/{movie_id}")
    fun getFilmDetails(
            @Path(value = "movie_id", encoded = true) filmId: Int,
    ): Observable<FilmDetail>

    @GET("search/movie")
    fun searchFilms(
            @Query("query") query: String? = null, @Query("include_adult") include_adult: Boolean = false
    ): Observable<FilmsResponse>

    companion object Factory {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w92/"
        const val API_KEY: String = BuildConfig.FILMS_API_KEY
        const val LANG_RU = "ru-RU"
        const val REGION_RU = "RU"
        private var retrofit: RetrofitServices? = null
        fun create(): RetrofitServices {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(createOkHttpClient(ApiInterceptor()))
                        .baseUrl("https://api.themoviedb.org/3/").build().create(RetrofitServices::class.java)
            }
            return retrofit!!
        }

        private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return httpClient.build()
        }

        class ApiInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                val url = request.url().newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .addQueryParameter("language", LANG_RU)
                        .build()
                val newRequest = request.newBuilder().url(url).build()
                return chain.proceed(newRequest)
            }
        }

    }

}