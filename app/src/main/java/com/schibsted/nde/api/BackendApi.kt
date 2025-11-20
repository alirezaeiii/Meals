package com.schibsted.nde.api

import com.schibsted.nde.model.MealsResponse
import retrofit2.http.GET

interface BackendApi {
    @GET("/api/json/v1/1/search.php?s=chicken")
    suspend fun getMeals(): MealsResponse
}