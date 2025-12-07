package com.schibsted.nde.feature.common

data class ViewState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String = "",
    val isWarning: Boolean = false,
)