package com.example.recipe.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.Meal
import com.example.recipe.data.MealDetailRepository
import com.example.recipe.data.MealResponse
import com.example.recipe.data.MealService
import kotlinx.coroutines.launch

class MealViewModel: ViewModel() {
    private val repository = MealDetailRepository(MealService.create())

    private val _meal = MutableLiveData<Meal?>(null)
    val meal: LiveData<Meal?> = _meal

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun searchMeal(mealName: String) {
        Log.d("MealVM", "loading data from API")

        viewModelScope.launch {
            _loading.value = true
            val result = repository.fetchMeal(mealName)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _meal.value = result.getOrNull()
        }
    }
}