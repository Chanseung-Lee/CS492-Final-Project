package com.example.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(application: Application) : AndroidViewModel(application) {
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals
    private val _randomMealName = MutableLiveData<String>()
    val randomMealName: LiveData<String> = _randomMealName

    fun searchMeal(mealName: String) {
        RetrofitInstance.api.searchMeal(mealName).enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    // Extract the list of meals from the response
                    val mealResponse = response.body()
                    _meals.postValue(mealResponse?.meals)

                    // Optional: Log the response for debugging
                    Log.d("MealViewModel", "Received ${mealResponse?.meals?.size} meals")
                } else {
                    // Handle the case where the response is not successful
                    Log.e("MealViewModel", "Response not successful")
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                // Handle failure in calling the API
                Log.e("MealViewModel", "API call failed: ${t.message}")
            }
        })
    }

}