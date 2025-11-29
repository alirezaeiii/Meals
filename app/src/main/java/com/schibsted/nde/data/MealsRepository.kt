package com.schibsted.nde.data

import android.content.Context
import com.schibsted.nde.api.BackendApi
import com.schibsted.nde.database.MealEntityDao
import com.schibsted.nde.database.asDomainModel
import com.schibsted.nde.di.IoDispatcher
import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.model.MealResponse
import com.schibsted.nde.model.asDatabaseModel
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
): BaseRepository<Meal, MealResponse>(context, dispatcher) {

    override suspend fun query(): List<Meal> = dao.getAll().asDomainModel()

    override suspend fun fetch(): List<MealResponse> = backendApi.getMeals().meals

    override suspend fun saveFetchResult(items: List<MealResponse>) {
        dao.insertAll(items.asDatabaseModel())
    }
}