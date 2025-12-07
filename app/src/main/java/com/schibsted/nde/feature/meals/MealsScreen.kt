package com.schibsted.nde.feature.meals

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.feature.common.Content
import com.schibsted.nde.feature.common.MealImage
import com.schibsted.nde.ui.typography
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun MealsScreen(
    viewModel: MealsViewModel = hiltViewModel(),
    navigateToDetail: (Meal) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(sheetContent = {
        val focusManager = LocalFocusManager.current
        if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
            DisposableEffect(Unit) {
                onDispose {
                    focusManager.clearFocus()
                }
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            var query by rememberSaveable { mutableStateOf("") }

            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Query") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.submitQuery(query)
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.align(Alignment.End)) {
                OutlinedButton(onClick = {
                    viewModel.submitQuery(null)
                    query = ""
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }) {
                    Text(text = "Clear")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    viewModel.submitQuery(query)
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }) {
                    Text(text = "Search")
                }
            }
        }
    }, sheetState = modalBottomSheetState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Food App")
                    },
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                modalBottomSheetState.show()
                            }
                        }) {
                            Icon(Icons.Filled.Search, "search")
                        }
                    }
                )
            },
            content = {
                MealsScreenContent(viewModel, navigateToDetail)
            }
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun MealsScreenContent(
    viewModel: MealsViewModel,
    navigateToDetail: (Meal) -> Unit
) {
    Content(viewModel) { state ->
        if (state.filteredMeals.isEmpty()) {
            Text(text = "No meals found")
        } else {
            val orientation = LocalConfiguration.current.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.filteredMeals) { meal ->
                        Divider(thickness = 8.dp)
                        MealRowComposable(meal, navigateToDetail)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.background(
                        MaterialTheme.colors.onSurface.copy(
                            alpha = 0.12f
                        )
                    )
                ) {
                    itemsIndexed(state.filteredMeals) { index, meal ->
                        val isFirstColumn = index % 2 == 0
                        Column(
                            Modifier.padding(
                                if (isFirstColumn) 8.dp else 4.dp,
                                8.dp,
                                if (isFirstColumn) 4.dp else 8.dp,
                                0.dp
                            )
                        ) {
                            MealRowComposable(meal, navigateToDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealRowComposable(
    meal: Meal,
    navigateToDetail: (Meal) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .clip(RoundedCornerShape(4.dp))
            .padding(16.dp)
            .clickable {
                navigateToDetail.invoke(meal)
            }
    ) {
        MealImage(meal.strMealThumb, Modifier.size(64.dp))
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    meal.strCategory,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                meal.strMeal, fontWeight = FontWeight.Bold,
                style = typography.h6
            )
        }
    }
}