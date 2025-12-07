package com.schibsted.nde.feature.common

interface BaseScreenState<T> {
    val base: ViewState<T>
    fun copyWithBase(base: ViewState<T>): BaseScreenState<T>
}