package com.schibsted.nde.base

interface BaseScreenState<T> {
    val base: ViewState<T>
    fun copyWithBase(base: ViewState<T>): BaseScreenState<T>
}