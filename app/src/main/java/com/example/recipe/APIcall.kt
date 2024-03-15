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

interface MealApiService {
    @GET("search.php")
    fun searchMeal(@Query("s") mealName: String): Call<MealResponse>

    @GET("random.php")
    fun getRandomMeal(): Call<MealResponse>
}

// Retrofit Instance
object RetrofitInstance {
    val api: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }
}


// ViewModel
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

    // Function to fetch a random meal
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    // Extract the list of meals (should be only one) from the response
                    val mealResponse = response.body()
                    _meals.postValue(mealResponse?.meals)
                    Log.d("MealViewModel", "Received random meal")
                } else {
                    Log.e("MealViewModel", "Response not successful")
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Log.e("MealViewModel", "API call failed: ${t.message}")
            }
        })
    }
}

fun formatMealForDisplay(meal: Meal): String {
    return buildString {
        append("Name: ${meal.strMeal}\n")
        append("Category: ${meal.strCategory}\n")
        append("Area: ${meal.strArea}\n")
        append("Instructions: ${meal.strInstructions}\n")
        append("Thumbnail URL: ${meal.strMealThumb}\n")
        append("Tags: ${meal.strTags}\n")
        append("YouTube Link: ${meal.strYoutube}\n")

        // Include ingredients and measurements
        append("Ingredients:\n")
        for (i in 1..20) {
            val ingredient = meal.getIngredient(i)
            val measure = meal.getMeasure(i)
            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                append("$ingredient: $measure\n")
            }
        }

        // Include other fields as needed
        append("Source: ${meal.strSource}\n")
        append("Image Source: ${meal.strImageSource}\n")
        append("Creative Commons Confirmed: ${meal.strCreativeCommonsConfirmed}\n")
        append("Date Modified: ${meal.dateModified}\n")
    }
}

// Extension functions to simplify accessing ingredients and measurements
fun Meal.getIngredient(index: Int): String? {
    return when (index) {
        1 -> this.strIngredient1
        2 -> this.strIngredient2
        3 -> this.strIngredient3
        4 -> this.strIngredient4
        5 -> this.strIngredient5
        6 -> this.strIngredient6
        7 -> this.strIngredient7
        8 -> this.strIngredient8
        9 -> this.strIngredient9
        10 -> this.strIngredient10
        11 -> this.strIngredient11
        12 -> this.strIngredient12
        13 -> this.strIngredient13
        14 -> this.strIngredient14
        15 -> this.strIngredient15
        16 -> this.strIngredient16
        17 -> this.strIngredient17
        18 -> this.strIngredient18
        19 -> this.strIngredient19
        20 -> this.strIngredient20
        else -> null
    }
}

fun Meal.getMeasure(index: Int): String? {
    return when (index) {
        1 -> this.strMeasure1
        2 -> this.strMeasure2
        3 -> this.strMeasure3
        4 -> this.strMeasure4
        5 -> this.strMeasure5
        6 -> this.strMeasure6
        7 -> this.strMeasure7
        8 -> this.strMeasure8
        9 -> this.strMeasure9
        10 -> this.strMeasure10
        11 -> this.strMeasure11
        12 -> this.strMeasure12
        13 -> this.strMeasure13
        14 -> this.strMeasure14
        15 -> this.strMeasure15
        16 -> this.strMeasure16
        17 -> this.strMeasure17
        18 -> this.strMeasure18
        19 -> this.strMeasure19
        20 -> this.strMeasure20
        else -> null
    }
}