package com.lessons.films

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

fun View.snackBarInfo(string: String) =
        Snackbar.make(this, string, Snackbar.LENGTH_SHORT).show()

fun View.snackBarReady() =
        Snackbar.make(this, resources.getString(R.string.ready), Snackbar.LENGTH_SHORT).show()

fun View.snackBarIntRes(stringResId: Int) =
        Snackbar.make(this, resources.getString(stringResId), Snackbar.LENGTH_SHORT).show()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_films, R.id.navigation_favorites))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}