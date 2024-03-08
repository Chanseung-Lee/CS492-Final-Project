package com.example.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        if (findViewById<FrameLayout>(R.id.nav_host_fragment) != null) {

            // However, if we're being restored from a previous state, then we don't need to do anything
            // and should return or else we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return
            }

            // Create an instance of the MealDetailFragment
            val mealDetailFragment = MealDetailFragment()

            // Add the fragment to the 'fragment_container' FrameLayout
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, mealDetailFragment).commit()
        }
    }
}