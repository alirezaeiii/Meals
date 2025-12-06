package com.schibsted.nde.feature.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T, V: ViewState<T>>: ViewModel() {

    abstract val state: StateFlow<V>

    abstract val showWarningUiEvent: SharedFlow<UiEvent>

    sealed class UiEvent {
        data class ShowWarning(val message: String) : UiEvent()
    }

    abstract fun refresh(isRefreshing: Boolean = false)
}