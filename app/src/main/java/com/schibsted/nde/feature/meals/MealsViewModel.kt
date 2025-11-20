package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.model.MealResponse
import com.schibsted.nde.utils.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealsRepository: BaseRepository<Meal, MealResponse>,
) : ViewModel() {
    private val _state = MutableStateFlow(MealsViewState(isLoading = true))

    val state: StateFlow<MealsViewState>
        get() = _state

    init {
        loadMeals()
    }

    fun loadMeals(query: String? = null) {
        viewModelScope.launch {

            mealsRepository.getResult().collectLatest { uiState ->
                when (uiState) {
                    is Async.Loading ->
                        _state.emit(
                            _state.value.copy(
                                isLoading = !uiState.isRefreshing,
                                isRefreshing = uiState.isRefreshing,
                                error = ""
                            )
                        )


                    is Async.Success -> {
                        val meals = uiState.data
                        query?.let {
                            submitQuery(query, meals)
                        } ?: run {
                            _state.emit(
                                _state.value.copy(
                                    meals = meals,
                                    filteredMeals = meals,
                                    isLoading = false,
                                    isRefreshing = false
                                )
                            )
                        }
                    }

                    is Async.Error -> {
                        _state.emit(
                            _state.value.copy(
                                error = uiState.message,
                                isWarning = uiState.isWarning,
                                isRefreshing = false,
                                isLoading = false
                            )
                        )
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
                filteredMeals = filteredMeals,
                isLoading = false,
                isRefreshing = false
            )
        )
    }
}