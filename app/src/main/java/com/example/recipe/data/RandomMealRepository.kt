package com.example.recipe.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandomMealRepository (
    private val service: MealService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var cachedRecipes: MealResponse? = null

    private val cacheMaxAge = 30 * 1000 // 15 seconds
    private var lastFetchTime = 0L

    suspend fun fetchRandomMeals(
        numberOfMeals: Int
    ) : Result<MealResponse?> {
        Log.d("RandomMealRepository", "fetching random meals $numberOfMeals")
        return if (shouldFetch()) {
            withContext(ioDispatcher) {
                try {
                    val mealsList = mutableListOf<Meal>()
                    repeat(numberOfMeals) {
                        val response = service.getRandomMeal()
                        if (response.isSuccessful) {
                            val mealResponse = response.body()
                            mealResponse?.meals?.let { mealsList.addAll(it) }
                            cachedRecipes = MealResponse(mealsList)
                            lastFetchTime = System.currentTimeMillis()
                            Result.success(cachedRecipes)
                        } else {
                            Result.failure(Exception(response.errorBody()?.string()))
                        }
                    }
                } catch (e: Exception) {
                    Result.failure<MealResponse?>(e)
                }
                Result.success(cachedRecipes)
            }
        } else {
            Result.success(cachedRecipes!!)
        }
    }

    private fun shouldFetch(): Boolean {
        val currentTime = System.currentTimeMillis()
        return cachedRecipes == null
                || (currentTime - lastFetchTime) > cacheMaxAge
    }

}