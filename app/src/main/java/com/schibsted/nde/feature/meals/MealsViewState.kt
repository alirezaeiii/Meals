package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.Meal
import com.schibsted.nde.feature.common.BaseScreenState
import com.schibsted.nde.feature.common.ViewState

data class MealsViewState(
    override val base: ViewState<Meal> = ViewState(),
    val filteredMeals: List<Meal> = emptyList(),
    val query: String? = null
) : BaseScreenState<Meal>