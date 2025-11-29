package com.schibsted.nde.di

import com.schibsted.nde.data.MealsRepository
import com.schibsted.nde.domain.BaseRepository
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.model.MealResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindRepository(repository: MealsRepository): BaseRepository<Meal, MealResponse>
}