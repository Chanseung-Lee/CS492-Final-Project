package com.example.recipe.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealDetailRepository (
    private val service: MealService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var selectedMeal: Meal? = null
    suspend fun fetchMeal(
        mealName: String
    ): Result<Meal?> {
        return withContext(ioDispatcher) {
            try {
                val response = service.searchMeal(mealName)
                if (response.isSuccessful) {
                    val mealResponse = response.body()
                    if (mealResponse != null && mealResponse.meals.isNotEmpty()) {
                        selectedMeal = mealResponse.meals.first()
                        Result.success(selectedMeal)
                    } else {
                        Result.failure(Exception("No meal found"))
                    }
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}