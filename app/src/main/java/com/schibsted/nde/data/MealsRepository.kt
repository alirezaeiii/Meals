package com.schibsted.nde.data

import com.schibsted.nde.api.BackendApi
import com.schibsted.nde.model.MealResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealsRepository @Inject constructor(
    private val backendApi: BackendApi,
) {
    suspend fun getMeals(): List<MealResponse> = backendApi.getMeals().meals
}