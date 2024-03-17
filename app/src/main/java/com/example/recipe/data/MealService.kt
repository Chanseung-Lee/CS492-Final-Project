package com.example.recipe.data
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealService {
    @GET("search.php")
    suspend fun searchMeal(@Query("s") mealName: String): Response<MealResponse>

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

        fun create(): MealService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MealService::class.java)
        }
    }
}