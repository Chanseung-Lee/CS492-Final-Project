package com.example.recipe.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.R
import com.example.recipe.data.Meal
import com.google.android.material.progressindicator.CircularProgressIndicator

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {
    private val viewModel: MainMenuViewModel by viewModels()
    private val recipeAdapter = MealAdapter()

    private lateinit var appheaderTV: TextView
    private lateinit var mainMenuRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appheaderTV = view.findViewById(R.id.tv_app_header)
        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        mainMenuRV = view.findViewById(R.id.rv_home)
        mainMenuRV.layoutManager = GridLayoutManager(requireContext(), 2)
        mainMenuRV.adapter = recipeAdapter

        val buttonClickListener = object : MealAdapter.OnButtonClickListener {
            override fun onButtonClick(recipe: Meal) {
                val bundle = Bundle().apply {
                    putString("mealName", recipe.strMeal)
                }
                findNavController().navigate(R.id.mealDetailFragment, bundle)
            }
        }
        recipeAdapter.setOnButtonClickListener(buttonClickListener)

        /*
         * Set up an observer on the recipe data.  If the mealNames >=2, display
         * it in the UI.
         */
        viewModel.randomMealNames.observe(viewLifecycleOwner, Observer { recipes ->
            Log.d("MainMenuFragment", recipes.toString())
            if (recipes !== null) {
                recipeAdapter.updateRecipes(recipes)
                mainMenuRV.visibility = View.VISIBLE
                mainMenuRV.scrollToPosition(0)
            }
        })

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching recipes: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                mainMenuRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchRandomMeals(4)
    }
}