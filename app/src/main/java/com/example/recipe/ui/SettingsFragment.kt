package com.example.recipe.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.example.recipe.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            PreferenceManager.getDefaultSharedPreferences(it)
                .registerOnSharedPreferenceChangeListener(this)
        }

        // Check current mode and set switch accordingly
        val isDarkModeEnabled = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        preferenceScreen.findPreference<SwitchPreference>("dark_mode")?.isChecked = isDarkModeEnabled
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            PreferenceManager.getDefaultSharedPreferences(it)
                .unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "dark_mode") {
            val isDarkModeEnabled = sharedPreferences?.getBoolean(key, false) ?: false
            // Apply dark mode or light mode based on the switch preference
            if (isDarkModeEnabled) {
                // Apply dark mode
                activity?.let { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            } else {
                // Apply light mode
                activity?.let { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
            }
            activity?.recreate() // Recreate activity to apply theme changes
        }
    }
}
