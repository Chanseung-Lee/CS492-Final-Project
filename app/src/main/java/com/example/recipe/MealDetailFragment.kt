package com.example.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
class MealDetailFragment : Fragment() {

    private lateinit var viewModel: MealViewModel

    private lateinit var imageViewMeal: ImageView
    private lateinit var textViewMealName: TextView
    private lateinit var textViewMealDetails: TextView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var textViewInstructions: TextView
    private lateinit var textViewAdditionalInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_meal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        imageViewMeal = view.findViewById(R.id.mealImage)
        textViewMealName = view.findViewById(R.id.mealName)
        textViewMealDetails = view.findViewById(R.id.mealDetails)
        linearLayoutIngredients = view.findViewById(R.id.ingredientsList)
        textViewInstructions = view.findViewById(R.id.instructionsText)
        textViewAdditionalInfo = view.findViewById(R.id.additionalInfo)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(MealViewModel::class.java)

        viewModel.meals.observe(viewLifecycleOwner, Observer { meals ->
            if (meals.isNotEmpty()) {
                val meal = meals.first() // Assuming we're displaying the first meal

                // Load meal image using Glide
                Glide.with(this)
                    .load(meal.strMealThumb)
                    .placeholder(R.drawable.meal_placeholder) // Optional: Placeholder image
                    .error(R.drawable.error_placeholder)     // Optional: Error placeholder image
                    .into(imageViewMeal)

                // Set Meal Name and Details
                textViewMealName.text = meal.strMeal
                textViewMealDetails.text = "${meal.strCategory} · ${meal.strArea}"

                // Set Ingredients
                linearLayoutIngredients.removeAllViews()
                getIngredientsList(meal).forEach { ingredient ->
                    val textView = TextView(context)
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
            // ... Repeat for all ingredients ...
        ).filter { it.isNotBlank() }
    }
}