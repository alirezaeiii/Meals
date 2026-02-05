package com.schibsted.nde.base

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
import com.schibsted.nde.feature.common.ErrorScreen
import com.schibsted.nde.feature.common.ProgressScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T, S : BaseScreenState<T>> Content(
    viewModel: BaseViewModel<T, S>,
    mainContent: @Composable (S) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.base.isLoading -> ProgressScreen()
            state.base.error.isNotEmpty() && !state.base.isWarning ->
                ErrorScreen(state.base.error) { viewModel.refresh() }

            else ->
                Column {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(state.base.isRefreshing),
                        onRefresh = { viewModel.refresh(true) },
                        indicator = { state, trigger -> SwipeRefreshIndicator(state, trigger) },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxSize()
                    ) {
                        mainContent(state)
                    }
                }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.showWarningUiEvent.collectLatest { event ->
            when (event) {
                is BaseViewModel.UiEvent.ShowWarning ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}