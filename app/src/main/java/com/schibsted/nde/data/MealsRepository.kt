package com.schibsted.nde.data

import com.schibsted.nde.api.BackendApi
import com.schibsted.nde.database.MealEntityDao
import com.schibsted.nde.database.asDomainModel
import com.schibsted.nde.di.IoDispatcher
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.model.asDatabaseModel
import com.schibsted.nde.utils.Async
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealsRepository @Inject constructor(
    private val backendApi: BackendApi,
    private val dao: MealEntityDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    private var cacheIsDirty = false
    fun getMeals(): Flow<Async<List<Meal>>> = flow {
        emit(Async.Loading)

        val dbMeals = dao.getAll()
        if (dbMeals.isEmpty()) {
            // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
            refresh(true)

            // ****** VIEW CACHE AS SINGLE SOURCE OF TRUTH ******
            emit(Async.Success(dao.getAll().asDomainModel()))
        } else if (cacheIsDirty) {
            // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
            refresh(false)

            // ****** VIEW CACHE AS SINGLE SOURCE OF TRUTH ******
            emit(Async.Success(dao.getAll().asDomainModel()))

            cacheIsDirty = false
        } else {
            // ****** VIEW CACHE AS SINGLE SOURCE OF TRUTH ******
            emit(Async.Success(dbMeals.asDomainModel()))
        }

    }.flowOn(dispatcher)

    fun refresh() {
        cacheIsDirty = true
    }

    private suspend fun refresh(isDbEmpty: Boolean) {
        if (!isDbEmpty) {
            dao.deleteAll()
        }
        val mealsResponse = backendApi.getMeals().meals
        dao.insertAll(mealsResponse.asDatabaseModel())
    }
}