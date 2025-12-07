package com.schibsted.nde.feature.meals

import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.feature.common.BaseViewModel
import com.schibsted.nde.feature.common.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    mealsRepository: BaseRepository<Meal>
) : BaseViewModel<Meal, MealsViewState>(
    mealsRepository,
    MealsViewState(base = ViewState(isLoading = true))
) {

    init {
        refresh()
    }

    override fun reduceLoading(old: MealsViewState, isRefreshing: Boolean): MealsViewState =
        old.copy(
            base = old.base.copy(
                isLoading = !isRefreshing,
                isRefreshing = isRefreshing,
                error = ""
            )
        )

    override suspend fun success(items: List<Meal>, isRefreshing: Boolean) {
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

    override fun reduceError(old: MealsViewState, msg: String, isWarning: Boolean): MealsViewState =
        old.copy(
            base = old.base.copy(
                error = msg,
                isWarning = isWarning,
                isLoading = false,
                isRefreshing = false
            )
        )

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
