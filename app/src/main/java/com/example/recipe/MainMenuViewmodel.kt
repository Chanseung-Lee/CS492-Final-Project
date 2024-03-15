package com.example.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {
    private val _randomMealNames = MutableLiveData<List<String>>()
    val randomMealNames: LiveData<List<String>> = _randomMealNames

    private var lastFetchTime = 0L // Timestamp of the last fetch

    fun fetchRandomMeals(numberOfMeals: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFetchTime < 15 * 1000) {
            // It's been less than 15 seconds since the last query, so don't fetch new data
            return
        }

        lastFetchTime = currentTime // Update the last fetch time
        val mealNames = mutableListOf<String>()

        repeat(numberOfMeals) {
            RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealResponse> {
                override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.meals?.firstOrNull()?.let { meal ->
                            mealNames.add(meal.strMeal)
                            if (mealNames.size == numberOfMeals) {
                                _randomMealNames.postValue(mealNames)
                            }
                        }
                    } else {
                        Log.e("MainMenuViewModel", "Response not successful")
                    }
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Log.e("MainMenuViewModel", "API call failed: ${t.message}")
                }
            })
        }
    }
}

