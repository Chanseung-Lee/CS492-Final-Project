package com.example.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealDetailTextIcon = view.findViewById<TextView>(R.id.text_icon_meal)
        mealDetailTextIcon.setOnClickListener {
            // Create a bundle and put the string data into it
            val bundle = Bundle()
            bundle.putString("mealName", mealDetailTextIcon.text.toString())

            // Navigate to MealDetailFragment with the bundle
            findNavController().navigate(R.id.mealDetailFragment, bundle)
        }
    }
}