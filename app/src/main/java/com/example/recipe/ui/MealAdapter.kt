package com.example.recipe.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe.R
import com.example.recipe.data.Meal
import com.example.recipe.data.MealResponse

class MealAdapter() : RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    private var recipes: MealResponse = MealResponse(emptyList())
    private var onButtonClickListener: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onButtonClick(recipe: Meal)
    }
    fun updateRecipes(recipes: MealResponse) {
        notifyItemRangeRemoved(0, recipes.meals.size)
        this.recipes = recipes
        notifyItemRangeInserted(0, recipes.meals.size)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = recipes.meals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes.meals[position])

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeIV: ImageView = itemView.findViewById(R.id.iv_recipe)
        private val recipeNameTV: TextView = itemView.findViewById(R.id.tv_recipe_name)
        private val recipeCuisineTV: TextView = itemView.findViewById(R.id.recipe_cuisine)
        private val recipeDetailButton: Button = itemView.findViewById(R.id.button_view_recipe)

        fun bind(recipe: Meal) {
            val ctx = itemView.context

            // Load meal image using Glide
            Glide.with(ctx)
                .load(recipe.strMealThumb)
                .placeholder(R.drawable.meal_placeholder)
                .error(R.drawable.error_placeholder)
                .into(recipeIV)

            recipeNameTV.text = recipe.strMeal
            recipeCuisineTV.text = recipe.strArea

            recipeDetailButton.setOnClickListener {
                onButtonClickListener?.onButtonClick(recipe)
            }
        }

    }
}