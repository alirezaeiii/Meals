package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.repository.BaseListRepository
import com.schibsted.nde.domain.model.Meal
import com.schibsted.nde.base.BaseViewModel
import com.schibsted.nde.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    mealsRepository: BaseListRepository<Meal>
) : BaseViewModel<Meal, MealsViewState>(
    mealsRepository,
    MealsViewState(base = ViewState(isLoading = true))
) {

    init {
        refresh()
    }

    override fun success(items: List<Meal>, isRefreshing: Boolean) {
        if (isRefreshing) {
            submitQuery(_state.value.query, items)
        } else {
            _state.value = MealsViewState(
                base = ViewState(
                    items = items,
                ),
                filteredMeals = items
            )
        }
    }

    fun submitQuery(query: String?, items: List<Meal>? = null) {
        val meals = items ?: _state.value.base.items

        val filtered = if (query.isNullOrBlank()) meals
        else meals.filter { it.strMeal.contains(query.trim(), ignoreCase = true) }

        _state.value = MealsViewState(
            query = query,
            filteredMeals = filtered,
            base = ViewState(
                items = meals
            )
        )
    }
}
