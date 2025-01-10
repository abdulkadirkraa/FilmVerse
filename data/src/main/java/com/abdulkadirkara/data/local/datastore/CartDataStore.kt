package com.abdulkadirkara.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Extension property to initialize and access the DataStore instance.
 *
 * The preferences are stored under the file name "app_preferences".
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

/**
 * A class for managing cart-related preferences using Jetpack DataStore.
 *
 * This class provides functionality to store and retrieve the cart item count in a
 * type-safe, asynchronous, and reactive manner.
 *
 * @property context The application context used to access the DataStore.
 * @constructor Injected using Hilt for dependency injection.
 */
class CartDataStore @Inject constructor(private val context: Context) {

    companion object {
        /**
         * The key used to store and retrieve the cart item count from the DataStore.
         */
        private val CART_ITEM_COUNT_KEY = intPreferencesKey("cart_item_count")
    }

    /**
     * A Flow that emits the current cart item count whenever it changes.
     *
     * This provides a reactive stream of data updates, making it suitable for real-time
     * UI updates or other asynchronous operations.
     */
    val cartItemCountFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CART_ITEM_COUNT_KEY] ?: 0
        }

    /**
     * Updates the cart item count in the DataStore.
     *
     * This function performs the operation asynchronously, ensuring that the
     * main thread remains unblocked.
     *
     * @param newCount The new cart item count to store in the DataStore.
     */
    suspend fun updateCartItemCount(newCount: Int) {
        context.dataStore.edit { preferences ->
            preferences[CART_ITEM_COUNT_KEY] = newCount
        }
    }
}