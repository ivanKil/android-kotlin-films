package com.lessons.films.network

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lessons.films.model.Repository

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val FILM_ID_EXTRA = "FilmId"

const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_LOAD_ERROR = "LOAD ERROR"

class DetailsService(name: String = "DetailService") : IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)
    private val repository: Repository = MovieRepository(RetrofitServices.create())

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            repository.getFilmDetails(intent.getIntExtra(FILM_ID_EXTRA, -1))
                    .subscribe({
                        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, it)
                        LocalBroadcastManager.getInstance(this)
                                .sendBroadcast(broadcastIntent)
                    }, {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(
                                broadcastIntent.apply { putExtra(DETAILS_LOAD_ERROR, it.message) })
                    })
        }
    }
}