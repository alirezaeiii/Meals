package com.schibsted.nde.utils

sealed class Async<out R> {
    object Loading : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
}