package com.lessons.films

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.lessons.films.connectcheck.NetworkChangeReceiver
import com.lessons.films.view.map.Geo
import com.lessons.films.view.map.GeoMyPosition

class MainActivity : MainActivityBasic() {
    override open fun menuElements() = setOf(
        R.id.navigation_films, R.id.navigation_favorites, R.id.navigation_history,
        R.id.navigation_contacts, R.id.navigation_map
    )
}