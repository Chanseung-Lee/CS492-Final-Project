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

        // Common OnClickListener for all meal text icons
        val mealClickListener = View.OnClickListener { textView ->
            val mealName = (textView as TextView).text.toString()
            navigateToMealDetail(mealName)
        }

        // Assign the listener to each TextView
        view.findViewById<TextView>(R.id.text_icon_meal1).setOnClickListener(mealClickListener)
        view.findViewById<TextView>(R.id.text_icon_meal2).setOnClickListener(mealClickListener)
        // Repeat for other TextViews
    }

    private fun navigateToMealDetail(mealName: String) {
        val bundle = Bundle().apply {
            putString("mealName", mealName)
        }
        findNavController().navigate(R.id.mealDetailFragment, bundle)
    }
}