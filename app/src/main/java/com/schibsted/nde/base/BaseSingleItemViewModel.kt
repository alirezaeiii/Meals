package com.schibsted.nde.base

import com.schibsted.nde.domain.repository.BaseRepository

abstract class BaseSingleItemViewModel<T, S: BaseScreenState<T>>(
    repository: BaseRepository<T>,
    initialState: S
) : BaseViewModel<T, T, S>(repository, initialState)