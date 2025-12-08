package com.schibsted.nde.domain.repository

import android.content.Context
import com.schibsted.nde.R
import com.schibsted.nde.utils.Async
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository<T>(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    protected abstract suspend fun query(): T?

    protected abstract suspend fun fetch(): T

    protected abstract suspend fun saveFetchResult(item: T)

    open fun getResult(): Flow<Async<T>> = flow {
        emit(Async.Loading())
        val dbData = query()
        dbData?.let {
            refresh(it)
        } ?: run {
            load()
        }
    }.flowOn(ioDispatcher)

    protected suspend fun FlowCollector<Async<T>>.load() {
        try {
            // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
            refresh()
            // ****** VIEW CACHE ******
            emit(Async.Success(query()!!))
        } catch (_: Throwable) {
            emit(Async.Error(context.getString(R.string.error_msg)))
        }
    }

    protected suspend fun FlowCollector<Async<T>>.refresh(dbData: T) {
        // ****** VIEW CACHE ******
        emit(Async.Success(dbData))
        emit(Async.Loading(true))
        try {
            // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
            refresh()
            // ****** VIEW CACHE ******
            emit(Async.Success(query()!!))
        } catch (_: Throwable) {
            emit(Async.Error(context.getString(R.string.refresh_error_msg), true))
        }
    }

    private suspend fun refresh() {
        saveFetchResult(fetch())
    }
}