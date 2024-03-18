package com.example.recipe.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.recipe.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(navController.graph)

        val appBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainMenuFragment -> {
                    true
                }

                R.id.refresh_recipes -> {
                    val mainMenuViewModel = MainMenuViewModel()
                    mainMenuViewModel.fetchRandomMeals(4)
                    findNavController(R.id.nav_host_fragment).navigate(R.id.mainMenuFragment)
                    true
                }

                R.id.settings -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.navigate_to_settings)
                    true
                }

                else -> false
            }
        }

        // Retrieve the current state of dark mode from SharedPreferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)

        // Apply dark or light based on stored switch preference at start (first install is light)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}