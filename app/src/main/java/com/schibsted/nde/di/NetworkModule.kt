package com.schibsted.nde.di

import com.schibsted.nde.api.BackendApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .build()

    @Singleton
    @Provides
    fun provideBackendApi(moshi: Moshi): BackendApi = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()
        .create(BackendApi::class.java)
}