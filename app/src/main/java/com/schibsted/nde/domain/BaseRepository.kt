package com.schibsted.nde.domain

import android.content.Context
import com.schibsted.nde.R
import com.schibsted.nde.utils.Async
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository<T, V>(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    protected abstract suspend fun query(): List<T>

    protected abstract suspend fun fetch(): List<V>

    protected abstract suspend fun saveFetchResult(items: List<V>)

    fun getResult(): Flow<Async<List<T>>> = flow {
        emit(Async.Loading())

        val dbData = query()
        if (dbData.isEmpty()) {
            try {
                // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** VIEW CACHE ******
                emit(Async.Success(query()))
            } catch (_: Throwable) {
                emit(Async.Error(context.getString(R.string.error_msg)))
            }

        } else {
            // ****** VIEW CACHE ******
            emit(Async.Success(dbData))
            try {
                emit(Async.Loading(true))
                // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** VIEW CACHE ******
                emit(Async.Success(query()))
            } catch (_: Throwable) {
                emit(Async.Error(context.getString(R.string.refresh_error_msg), true))
            }
        }
    }.flowOn(ioDispatcher)

    private suspend fun refresh() {
        saveFetchResult(fetch())
    }
}