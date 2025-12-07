package com.schibsted.nde.feature.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.utils.Async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, S>(
    private val repository: BaseRepository<T>,
    initialState: S
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S>
        get() = _state

    private val _showWarningUiEvent = MutableSharedFlow<UiEvent>()
    val showWarningUiEvent = _showWarningUiEvent

    sealed class UiEvent {
        data class ShowWarning(val message: String) : UiEvent()
    }

    protected abstract fun reduceLoading(old: S, isRefreshing: Boolean): S

    protected abstract suspend fun success(items: List<T>, isRefreshing: Boolean)

    protected abstract fun reduceError(old: S, msg: String, isWarning: Boolean): S

    // Keep your refresh logic, but rewrite to use the reducer:
    fun refresh(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            repository.getResult().collectLatest { uiState ->
                when (uiState) {
                    is Async.Loading -> {
                        updateState { old ->
                            reduceLoading(old, uiState.isRefreshing)
                        }
                    }

                    is Async.Success -> success(uiState.data, isRefreshing)

                    is Async.Error -> {
                        updateState { old ->
                            reduceError(old, uiState.message, uiState.isWarning)
                        }
                        if (uiState.isWarning) {
                            _showWarningUiEvent.emit(UiEvent.ShowWarning(uiState.message))
                        }
                    }
                }
            }
        }
    }

    private fun updateState(reducer: (S) -> S) {
        _state.value = reducer(_state.value)
    }
}
