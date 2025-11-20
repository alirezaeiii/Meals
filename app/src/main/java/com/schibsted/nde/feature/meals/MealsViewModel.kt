package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.data.MealsRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.utils.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    fun loadMeals(query: String? = null) {
        viewModelScope.launch {

            mealsRepository.getMeals().collectLatest { uiState ->
                when (uiState) {
                    is Async.Loading ->
                        _state.emit(_state.value.copy(isLoading = true))

                    is Async.Success -> {
                        _state.emit(_state.value.copy(isLoading = false))
                        val meals = uiState.data
                        query?.let {
                            mealsRepository.refresh()
                            submitQuery(query, meals)
                        } ?: run {
                            _state.emit(
                                _state.value.copy(
                                    meals = meals,
                                    filteredMeals = meals
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun submitQuery(
        query: String?,
        meals: List<Meal> = emptyList()
    ) {
        if (meals.isEmpty()) {
            executeQuery(query, _state.value.meals)
        } else {
            executeQuery(query, meals)
        }
    }

    suspend fun executeQuery(
        query: String?,
        meals: List<Meal>
    ) {
        val filteredMeals = if (query?.isBlank() == true) {
            meals
        } else {
            meals.filter {
                it.strMeal.lowercase().contains(query?.lowercase()?.trim() ?: "")
            }
        }
        _state.emit(
            _state.value.copy(
                query = query,
                meals = meals,
                filteredMeals = filteredMeals
            )
        )
    }
}