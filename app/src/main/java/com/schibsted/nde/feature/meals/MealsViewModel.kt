package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.model.MealResponse
import com.schibsted.nde.utils.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _showWarningUiEvent = MutableSharedFlow<UiEvent>()
    val showWarningUiEvent = _showWarningUiEvent.asSharedFlow()

    sealed class UiEvent {
        data class ShowWarning(val message: String) : UiEvent()
    }

    init {
        loadMeals()
    }

    fun loadMeals(isRefreshing: Boolean = false) {
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
                        if (isRefreshing) {
                            submitQuery(_state.value.query, meals)
                        } else {
                            _state.emit(MealsViewState(meals = meals, filteredMeals = meals))
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
                        if (uiState.isWarning) {
                            _showWarningUiEvent.emit(UiEvent.ShowWarning(uiState.message))
                        }
                    }
                }
            }
        }
    }

    fun submitQuery(
        query: String?,
        items: List<Meal>? = null
    ) {
        val meals = items ?: _state.value.meals
        val filteredMeals = if (query.isNullOrBlank()) {
            meals
        } else {
            meals.filter {
                it.strMeal.contains(query.trim(), ignoreCase = true)
            }
        }
        _state.value = MealsViewState(
            query = query,
            meals = meals,
            filteredMeals = filteredMeals
        )
    }
}