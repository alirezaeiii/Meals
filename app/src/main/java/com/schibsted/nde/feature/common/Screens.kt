package com.schibsted.nde.feature.common

sealed class Screens(val title: String) {
    object Meals : Screens("meals_screen")
    object Details : Screens("details_screen/{$MEAL}")

    companion object {
        const val MEAL = "meal"
    }
}