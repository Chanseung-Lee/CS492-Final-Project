package com.example.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MealViewModel
    private lateinit var textViewRecipeDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        // Initialize textViewRecipeName
        textViewRecipeDetails = findViewById(R.id.textViewRecipeName)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MealViewModel::class.java)

        viewModel.meals.observe(this, Observer { meals ->
            if (meals.isNotEmpty()) {
                val formattedMeals = meals.joinToString("\n\n") { meal ->
                    formatMealForDisplay(meal)
                }
                // Assuming you have a TextView to display the formatted meals
                textViewRecipeDetails.text = formattedMeals
            }
        })

        viewModel.searchMeal("Arrabiata")
    }
}