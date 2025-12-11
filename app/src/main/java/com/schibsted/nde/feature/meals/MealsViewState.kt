package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.model.Meal
import com.schibsted.nde.base.BaseScreenState
import com.schibsted.nde.base.ViewState

data class MealsViewState(
    override val base: ViewState<List<Meal>> = ViewState(),
    val filteredMeals: List<Meal> = emptyList(),
    val query: String? = null
) : BaseScreenState<List<Meal>> {

    override fun copyWithBase(base: ViewState<List<Meal>>) = copy(base = base)
}