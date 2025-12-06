package com.schibsted.nde.domain

import com.schibsted.nde.database.MealEntity

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strMealThumb: String,
    val strYoutube: String?,
    val strInstructions: String
)

fun List<Meal>.asDatabaseModel() = map(Meal::asDatabaseModel)

private fun Meal.asDatabaseModel() = MealEntity(
    id = idMeal,
    strMeal = strMeal,
    strCategory = strCategory,
    strMealThumb = strMealThumb,
    strYoutube = strYoutube,
    strInstructions = strInstructions
)