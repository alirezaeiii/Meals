package com.schibsted.nde.feature.meals

import com.schibsted.nde.model.MealResponse

data class MealsViewState(
    val meals: List<MealResponse> = emptyList(),
    val filteredMeals: List<MealResponse> = emptyList(),
    val isLoading: Boolean = false,
    val query: String? = null
)