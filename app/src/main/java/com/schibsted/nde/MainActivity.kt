package com.schibsted.nde

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.schibsted.nde.feature.common.Screens
import com.schibsted.nde.feature.common.Screens.Companion.MEAL
import com.schibsted.nde.feature.details.DetailsScreen
import com.schibsted.nde.feature.meals.MealsScreen
import com.schibsted.nde.model.MealResponse
import com.schibsted.nde.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph(navController)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screens.Meals.title) {
        composable(Screens.Meals.title) {
            MealsScreen(hiltViewModel()) { meal ->
                val json =
                    Uri.encode(Gson().toJson(meal, object : TypeToken<MealResponse>() {}.type))
                navController.navigate(
                    Screens.Details.title.replace
                        ("{${MEAL}}", json)
                )
            }
        }
        composable(
            Screens.Details.title, arguments = listOf(
                navArgument(MEAL) {
                    type = NavType.StringType
                }
            )
        ) { from ->
            DetailsScreen(
                Gson().fromJson(
                    from.arguments?.getString(MEAL),
                    object : TypeToken<MealResponse>() {}.type,
                )
            ) {
                navController.navigateUp()
            }
        }
    }
}