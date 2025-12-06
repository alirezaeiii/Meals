package com.schibsted.nde.feature.common

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun <T, V : ViewState<T>> Content(
    viewModel: BaseViewModel<T, V>,
    mainContent: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            ProgressScreen()
        }

        if (state.error.isNotEmpty() && !state.isWarning) {
            ErrorScreen(state.error) { viewModel.refresh() }
        }

        val context = LocalContext.current
        LaunchedEffect(Unit) {
            viewModel.showWarningUiEvent.collect { event ->
                when (event) {
                    is BaseViewModel.UiEvent.ShowWarning ->
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        Column {
            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isRefreshing),
                onRefresh = { viewModel.refresh(true) },
                indicator = { state, trigger -> SwipeRefreshIndicator(state, trigger) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxSize()
            ) {
                if (!state.isLoading && (state.error.isEmpty() || state.isWarning)) {
                    mainContent()
                }
            }
        }
    }
}