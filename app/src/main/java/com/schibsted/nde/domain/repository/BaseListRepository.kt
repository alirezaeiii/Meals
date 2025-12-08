package com.schibsted.nde.domain.repository

import android.content.Context
import com.schibsted.nde.utils.Async
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseListRepository<T>(
    context: Context,
    ioDispatcher: CoroutineDispatcher
) : BaseRepository<List<T>>(context, ioDispatcher) {

    override fun getResult(): Flow<Async<List<T>>> = flow {
        emit(Async.Loading())
        val dbData = query()
        if (dbData.isNullOrEmpty()) {
            load()
        } else {
            refresh(dbData)
        }
    }
}