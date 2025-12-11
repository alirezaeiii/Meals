package com.schibsted.nde.di

import com.schibsted.nde.data.repository.MealsRepository
import com.schibsted.nde.domain.model.Meal
import com.schibsted.nde.domain.repository.BaseRepository
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
    @JvmSuppressWildcards
    internal abstract fun bindRepository(repository: MealsRepository): BaseRepository<List<Meal>>
}