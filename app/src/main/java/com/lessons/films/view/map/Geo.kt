package com.lessons.films.view.map

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.lessons.films.Cinema
import com.lessons.films.R


class Geo(val activity: Activity) {
    private val REQUEST_CODE_LOCATION = 1234
    private val RADIUS_METERS = 500f
    private lateinit var geofencingClient: GeofencingClient
    private val geofenceList = mutableListOf<Geofence>()

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun initGeofence() {
        geofencingClient = LocationServices.getGeofencingClient(activity)
        addGeofences()
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), REQUEST_CODE_LOCATION
            )
            return
        } else {
            addGeofencesToClient()
        }
    }

    private fun addGeofencesToClient() {
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)
                .run {
                    addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.add_geofence),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    addOnFailureListener {
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.error_add_geofence),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (ActivityCompat.checkSelfPermission(
                        activity, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    addGeofencesToClient()
                }
            }
        }
    }

    private fun addGeofences() {
        for (c in listOf(
            Cinema("Победа", 55.1091006728, 82.9861796847),
            Cinema("Маяковский", 55.079234, 82.7234),
            Cinema("Синема Парк", 55.0845, 82.85364)
        ))
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId(c.name)
                    .setCircularRegion(c.latitude, c.longitude, RADIUS_METERS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
                    ).build()
            )
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    fun removeGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener { }
            addOnFailureListener { }
        }
    }


}