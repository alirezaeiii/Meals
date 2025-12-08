package com.schibsted.nde.data.repository

import android.content.Context
import com.schibsted.nde.data.api.BackendApi
import com.schibsted.nde.data.database.MealEntityDao
import com.schibsted.nde.data.database.asDomainModel
import com.schibsted.nde.data.response.asDomainModel
import com.schibsted.nde.di.IoDispatcher
import com.schibsted.nde.domain.repository.BaseListRepository
import com.schibsted.nde.domain.model.Meal
import com.schibsted.nde.domain.model.asDatabaseModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealsRepository @Inject constructor(
    private val backendApi: BackendApi,
    private val dao: MealEntityDao,
    @ApplicationContext context: Context,
    @IoDispatcher dispatcher: CoroutineDispatcher
): BaseListRepository<Meal>(context, dispatcher) {

    override suspend fun query(): List<Meal> = dao.getAll().asDomainModel()

    override suspend fun fetch(): List<Meal> = backendApi.getMeals().meals.asDomainModel()

    override suspend fun saveFetchResult(item: List<Meal>) {
        dao.insertAll(item.asDatabaseModel())
    }
}