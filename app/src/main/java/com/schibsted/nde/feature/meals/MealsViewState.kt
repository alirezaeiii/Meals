package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.Meal
import com.schibsted.nde.base.BaseScreenState
import com.schibsted.nde.base.ViewState

data class MealsViewState(
    override val base: ViewState<Meal> = ViewState(),
    val filteredMeals: List<Meal> = emptyList(),
    val query: String? = null
) : BaseScreenState<Meal> {

    override fun copyWithBase(base: ViewState<Meal>) = copy(base = base)
}