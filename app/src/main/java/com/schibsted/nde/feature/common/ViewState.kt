package com.schibsted.nde.feature.common

abstract class ViewState<T> {
    abstract val items: List<T>
    abstract val isLoading: Boolean
    abstract val isRefreshing: Boolean
    abstract val error: String
    abstract val isWarning: Boolean
}