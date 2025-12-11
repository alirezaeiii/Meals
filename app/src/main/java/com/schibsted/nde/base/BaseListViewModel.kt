package com.schibsted.nde.base

import com.schibsted.nde.domain.repository.BaseRepository

abstract class BaseListViewModel<T, S: BaseScreenState<T>>(
    repository: BaseRepository<List<T>>,
    initialState: S
) : BaseViewModel<List<T>, T, S>(repository, initialState)