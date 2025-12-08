package com.schibsted.nde.data.response

import com.schibsted.nde.domain.model.Meal

data class MealResponse(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strMealThumb: String,
    val strYoutube: String?,
    val strInstructions: String
)

fun List<MealResponse>.asDomainModel() = map(MealResponse::asDomainModel)

private fun MealResponse.asDomainModel() = Meal(
    idMeal = idMeal,
    strMeal = strMeal,
    strCategory = strCategory,
    strMealThumb = strMealThumb,
    strYoutube = strYoutube,
    strInstructions = strInstructions
)