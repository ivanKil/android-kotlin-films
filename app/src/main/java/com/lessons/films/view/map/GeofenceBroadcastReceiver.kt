package com.lessons.films.view.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.lessons.films.R

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG: String = "GeofenceBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            Log.i(
                TAG, "-------------------------геозона "
                        + geofencingEvent.triggeringGeofences.get(0).requestId
            )
            Toast.makeText(
                context,
                String.format(
                    context!!.resources.getString(R.string.event_geo),
                    geofencingEvent.triggeringGeofences.get(0).requestId
                ), Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.e(TAG, context!!.getString(R.string.geofence_transition_invalid_type))
        }
    }
}
