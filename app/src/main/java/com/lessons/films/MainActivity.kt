package com.lessons.films

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.lessons.films.connectcheck.NetworkChangeReceiver

fun View.snackBarInfo(string: String) =
        Snackbar.make(this, string, Snackbar.LENGTH_SHORT).show()

fun View.snackBarError(string: String) {
    var sb = Snackbar.make(this, "${resources.getString(R.string.error)} : $string", Snackbar.LENGTH_LONG)
    sb.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
    sb.show()
}

fun View.snackBarReady() =
        Snackbar.make(this, resources.getString(R.string.ready), Snackbar.LENGTH_SHORT).show()

fun View.snackBarIntRes(stringResId: Int) =
        Snackbar.make(this, resources.getString(stringResId), Snackbar.LENGTH_SHORT).show()

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

class MainActivity : AppCompatActivity() {
    private val receiver = NetworkChangeReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_films, R.id.navigation_favorites, R.id.navigation_history, R.id.navigation_contacts))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}