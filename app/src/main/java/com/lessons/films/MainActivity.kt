package com.lessons.films

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lessons.films.view.FilmsFavorite
import com.lessons.films.view.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_films, R.id.navigation_favorites))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //

        val bnv: BottomNavigationView = findViewById(R.id.nav_view)
        bnv.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_films ->
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, HomeFragment.newInstance())
                            .commitNow()
                R.id.action_favorites -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, FilmsFavorite.newInstance())
                            .commitNow()
                }
            }
            true
        }
    }
}