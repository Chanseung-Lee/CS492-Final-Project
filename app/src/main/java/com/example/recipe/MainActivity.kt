package com.example.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MealViewModel

    private lateinit var imageViewMeal: ImageView
    private lateinit var textViewMealName: TextView
    private lateinit var textViewMealDetails: TextView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var textViewInstructions: TextView
    private lateinit var textViewAdditionalInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        // Initialize Views
        imageViewMeal = findViewById(R.id.mealImage)
        textViewMealName = findViewById(R.id.mealName)
        textViewMealDetails = findViewById(R.id.mealDetails)
        linearLayoutIngredients = findViewById(R.id.ingredientsList)
        textViewInstructions = findViewById(R.id.instructionsText)
        textViewAdditionalInfo = findViewById(R.id.additionalInfo)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MealViewModel::class.java)

        viewModel.meals.observe(this, Observer { meals ->
            if (meals.isNotEmpty()) {
                val meal = meals.first() // Assuming we're displaying the first meal

                // Set Meal Image
                // Load image using a library like Glide or Picasso
                // Glide.with(this).load(meal.strMealThumb).into(imageViewMeal)

                // Set Meal Name and Details
                textViewMealName.text = meal.strMeal
                textViewMealDetails.text = "${meal.strCategory} · ${meal.strArea}"

                // Set Ingredients
                linearLayoutIngredients.removeAllViews()
                val ingredients = getIngredientsList(meal)
                ingredients.forEach { ingredient ->
                    val textView = TextView(this)
                    textView.text = ingredient
                    linearLayoutIngredients.addView(textView)
                }

                // Set Instructions
                textViewInstructions.text = meal.strInstructions

                // Set Additional Info
                textViewAdditionalInfo.text = "Tags: ${meal.strTags}\nYouTube: ${meal.strYoutube}"
            }
        })

        viewModel.searchMeal("Arrabiata")
    }

    private fun getIngredientsList(meal: Meal): List<String> {
        return listOfNotNull(
            meal.strIngredient1?.let { "${it}: ${meal.strMeasure1}" },
            meal.strIngredient2?.let { "${it}: ${meal.strMeasure2}" },
            // Repeat for all ingredients...
        ).filter { it.isNotBlank() }
    }

    // Add formatMealForDisplay function if needed
}