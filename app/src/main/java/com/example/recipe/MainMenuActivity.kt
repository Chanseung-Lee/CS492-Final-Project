package com.example.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment() {

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainMenuViewModel::class.java]

        viewModel.randomMealNames.observe(viewLifecycleOwner, Observer { mealNames ->
            if (mealNames.size >= 2) {
                updateTextViews(mealNames[0], mealNames[1])
            }
        })

        viewModel.fetchRandomMeals(2) // Fetch 2 random meals
    }

    private fun updateTextViews(mealName1: String, mealName2: String) {
        val textViewMeal1 = view?.findViewById<TextView>(R.id.text_icon_meal1)
        val textViewMeal2 = view?.findViewById<TextView>(R.id.text_icon_meal2)

        textViewMeal1?.apply {
            text = mealName1
            setOnClickListener { navigateToMealDetail(mealName1) }
        }

        textViewMeal2?.apply {
            text = mealName2
            setOnClickListener { navigateToMealDetail(mealName2) }
        }
    }

    private fun navigateToMealDetail(mealName: String) {
        val bundle = Bundle().apply {
            putString("mealName", mealName)
        }
        findNavController().navigate(R.id.mealDetailFragment, bundle)
    }
}