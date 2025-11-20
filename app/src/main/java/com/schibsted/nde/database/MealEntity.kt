package com.schibsted.nde.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.schibsted.nde.domain.Meal

@Entity(tableName = "meal")
data class MealEntity(
    @PrimaryKey val id: String,
    val strMeal: String,
    val strCategory: String,
    val strMealThumb: String,
    val strYoutube: String?
)

fun List<MealEntity>.asDomainModel() = map(MealEntity::asDomainModel)

private fun MealEntity.asDomainModel() = Meal(
    idMeal = id,
    strMeal = strMeal,
    strCategory = strCategory,
    strMealThumb = strMealThumb,
    strYoutube = strYoutube
)
