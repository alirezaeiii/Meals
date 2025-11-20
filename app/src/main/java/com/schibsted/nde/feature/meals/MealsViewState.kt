package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.Meal

data class MealsViewState(
    val meals: List<Meal> = emptyList(),
    val filteredMeals: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val query: String? = null
)