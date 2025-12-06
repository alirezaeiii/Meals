package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.Meal
import com.schibsted.nde.feature.common.ViewState

data class MealsViewState(
    override val items: List<Meal> = emptyList(),
    override val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    override val error: String = "",
    override val isWarning: Boolean = false,
    val filteredMeals: List<Meal> = emptyList(),
    val query: String? = null,
): ViewState<Meal>()