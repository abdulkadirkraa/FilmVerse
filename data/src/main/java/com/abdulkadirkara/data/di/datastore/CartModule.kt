package com.abdulkadirkara.data.di.datastore

import android.content.Context
import com.abdulkadirkara.data.local.datastore.CartDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies related to cart data datastore.
 * [CartDataStore] is provided as a singleton instance throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    /**
     * Provides an instance of [CartDataStore].
     * It is created using the [Context] and provided as a singleton across the application.
     *
     * @param context The application context.
     * @return An instance of [CartDataStore].
     */
    @Provides
    @Singleton
    fun provideCartDataStore(@ApplicationContext context: Context): CartDataStore {
        return CartDataStore(context)
    }
}