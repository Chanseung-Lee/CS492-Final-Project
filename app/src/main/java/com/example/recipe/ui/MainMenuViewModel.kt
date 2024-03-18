package com.example.recipe.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.RandomMealRepository
import com.example.recipe.data.MealResponse
import com.example.recipe.data.MealService
import kotlinx.coroutines.launch

class MainMenuViewModel: ViewModel() {
    private val repository = RandomMealRepository(MealService.create())

    private val _randomMealNames = MutableLiveData<MealResponse?>(null)
    val randomMealNames: LiveData<MealResponse?> = _randomMealNames

    private val _error = MutableLiveData<Throwable?>(null)

    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)

    val loading: LiveData<Boolean> = _loading

    fun fetchRandomMeals(numberOfMeals: Int) {
        Log.d("MainMenuVM", "loading data from API $numberOfMeals")

        viewModelScope.launch {
            _loading.value = true
            val result = repository.fetchRandomMeals(numberOfMeals)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _randomMealNames.value = result.getOrNull()
        }
    }
}

