package com.schibsted.nde.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.domain.repository.BaseRepository
import com.schibsted.nde.utils.Async
import com.schibsted.nde.utils.withBase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, S: BaseScreenState<T>>(
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

    protected abstract fun success(items: T, isRefreshing: Boolean)

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

    private fun reduceLoading(old: S, isRefreshing: Boolean): S =
        old.withBase(
            old.base.copy(
                isLoading = !isRefreshing,
                isRefreshing = isRefreshing,
                error = ""
            )
        )

    private fun reduceError(old: S, msg: String, isWarning: Boolean): S =
        old.withBase(
            old.base.copy(
                isLoading = false,
                isRefreshing = false,
                error = msg,
                isWarning = isWarning
            )
        )
}