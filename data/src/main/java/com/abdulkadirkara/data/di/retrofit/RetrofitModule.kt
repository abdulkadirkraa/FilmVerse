package com.abdulkadirkara.data.di.retrofit

import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.data.remote.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies related to Retrofit and API services.
 * This module provides a singleton instance of Retrofit and ApiService.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides a singleton instance of Retrofit.
     * It is configured with the base URL and Gson converter for JSON serialization.
     *
     * @return A singleton [Retrofit] instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL) // Base URL for API
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter to handle JSON
            .build()
    }

    /**
     * Provides a singleton instance of [ApiService].
     * This service is used to make API requests through Retrofit.
     *
     * @param retrofit The [Retrofit] instance to be used.
     * @return A singleton [ApiService] instance.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}