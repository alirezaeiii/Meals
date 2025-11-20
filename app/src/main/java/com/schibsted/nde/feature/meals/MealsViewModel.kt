package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.data.MealsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MealsViewState(isLoading = true))

    val state: StateFlow<MealsViewState>
        get() = _state

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isLoading = true))
            val meals = mealsRepository.getMeals()
            _state.emit(_state.value.copy(meals = meals, filteredMeals = meals))
            _state.emit(_state.value.copy(isLoading = false))
        }
    }

    fun submitQuery(query: String?) {
        viewModelScope.launch {
            val filteredMeals = if (query?.isNotBlank() == true) {
                _state.value.meals
            } else {
                _state.value.meals.filter {
                    it.strMeal.lowercase().contains(query?.lowercase() ?: "")
                }
            }
            _state.emit(_state.value.copy(query = query, filteredMeals = filteredMeals))
        }
    }
}