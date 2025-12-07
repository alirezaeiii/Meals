package com.schibsted.nde.utils

import com.schibsted.nde.base.BaseScreenState
import com.schibsted.nde.base.ViewState

@Suppress("UNCHECKED_CAST")
fun <T, S : BaseScreenState<T>> S.withBase(newBase: ViewState<T>): S {
    return copyWithBase(newBase) as S
}