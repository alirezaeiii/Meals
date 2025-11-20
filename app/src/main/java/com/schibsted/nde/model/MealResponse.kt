package com.schibsted.nde.model

import com.schibsted.nde.database.MealEntity

data class MealResponse(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strMealThumb: String,
    val strYoutube: String?,
)

fun List<MealResponse>.asDatabaseModel() = map(MealResponse::asDatabaseModel)

private fun MealResponse.asDatabaseModel() = MealEntity(
    id = idMeal,
    strMeal = strMeal,
    strCategory = strCategory,
    strMealThumb = strMealThumb,
    strYoutube = strYoutube
)