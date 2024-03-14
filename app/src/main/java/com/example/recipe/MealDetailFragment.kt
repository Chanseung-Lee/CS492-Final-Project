package com.example.recipe

import android.content.Intent
import android.net.Uri
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
    private lateinit var textViewTags: TextView
    private lateinit var textViewYouTubeLink: TextView

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
        textViewTags = view.findViewById(R.id.tagsText)
        textViewYouTubeLink = view.findViewById(R.id.youtubeLinkText)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(MealViewModel::class.java)

        // Retrieve the meal name from the arguments
        val mealName = arguments?.getString("mealName") ?: "Arrabiata" // Default value if no argument is passed

        viewModel.meals.observe(viewLifecycleOwner, Observer { meals ->
            if (meals.isNotEmpty()) {
                val meal = meals.first() // Use the first meal from the response

                // Load meal image using Glide
                Glide.with(this)
                    .load(meal.strMealThumb)
                    .placeholder(R.drawable.meal_placeholder)
                    .error(R.drawable.error_placeholder)
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

                // Set Tags
                textViewTags.text = "Tags: ${meal.strTags}"

                // Set YouTube Link
                textViewYouTubeLink.apply {
                    text = "YouTube Link"
                    paint.isUnderlineText = true
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
                        startActivity(intent)
                    }
                }
            }
        })

        // Use the retrieved meal name for the API query
        viewModel.searchMeal(mealName)
    }

    private fun getIngredientsList(meal: Meal): List<String> {
        return listOfNotNull(
            if (!meal.strIngredient1.isNullOrBlank()) "${meal.strIngredient1}: ${meal.strMeasure1}" else null,
            if (!meal.strIngredient2.isNullOrBlank()) "${meal.strIngredient2}: ${meal.strMeasure2}" else null,
            if (!meal.strIngredient3.isNullOrBlank()) "${meal.strIngredient3}: ${meal.strMeasure3}" else null,
            if (!meal.strIngredient4.isNullOrBlank()) "${meal.strIngredient4}: ${meal.strMeasure4}" else null,
            if (!meal.strIngredient5.isNullOrBlank()) "${meal.strIngredient5}: ${meal.strMeasure5}" else null,
            if (!meal.strIngredient6.isNullOrBlank()) "${meal.strIngredient6}: ${meal.strMeasure6}" else null,
            if (!meal.strIngredient7.isNullOrBlank()) "${meal.strIngredient7}: ${meal.strMeasure7}" else null,
            if (!meal.strIngredient8.isNullOrBlank()) "${meal.strIngredient8}: ${meal.strMeasure8}" else null,
            if (!meal.strIngredient9.isNullOrBlank()) "${meal.strIngredient9}: ${meal.strMeasure9}" else null,
            if (!meal.strIngredient10.isNullOrBlank()) "${meal.strIngredient10}: ${meal.strMeasure10}" else null,
            if (!meal.strIngredient11.isNullOrBlank()) "${meal.strIngredient11}: ${meal.strMeasure11}" else null,
            if (!meal.strIngredient12.isNullOrBlank()) "${meal.strIngredient12}: ${meal.strMeasure12}" else null,
            if (!meal.strIngredient13.isNullOrBlank()) "${meal.strIngredient13}: ${meal.strMeasure13}" else null,
            if (!meal.strIngredient14.isNullOrBlank()) "${meal.strIngredient14}: ${meal.strMeasure14}" else null,
            if (!meal.strIngredient15.isNullOrBlank()) "${meal.strIngredient15}: ${meal.strMeasure15}" else null,
            if (!meal.strIngredient16.isNullOrBlank()) "${meal.strIngredient16}: ${meal.strMeasure16}" else null,
            if (!meal.strIngredient17.isNullOrBlank()) "${meal.strIngredient17}: ${meal.strMeasure17}" else null,
            if (!meal.strIngredient18.isNullOrBlank()) "${meal.strIngredient18}: ${meal.strMeasure18}" else null,
            if (!meal.strIngredient19.isNullOrBlank()) "${meal.strIngredient19}: ${meal.strMeasure19}" else null,
            if (!meal.strIngredient20.isNullOrBlank()) "${meal.strIngredient20}: ${meal.strMeasure20}" else null
        )
    }
}