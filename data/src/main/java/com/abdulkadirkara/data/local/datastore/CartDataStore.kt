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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class CartDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val CART_ITEM_COUNT_KEY = intPreferencesKey("cart_item_count")

    // Get the cart item count
    val cartItemCountFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CART_ITEM_COUNT_KEY] ?: 0
        }

    // Update the cart item count
    suspend fun updateCartItemCount(newCount: Int) {
        context.dataStore.edit { preferences ->
            preferences[CART_ITEM_COUNT_KEY] = newCount
        }
    }
}