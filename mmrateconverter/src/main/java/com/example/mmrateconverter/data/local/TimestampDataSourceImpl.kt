package com.example.mmrateconverter.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

// DataStore Instance ကို App အတွင်း တစ်ခါတည်း သုံးနိုင်ရန် ကြေညာခြင်း
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rate_prefs")

@Singleton
class TimestampDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TimestampDataSource {

    private val dataStore = context.dataStore

    override suspend fun saveLastFetchTime(key: String, time: Long) {
        dataStore.edit { preferences ->
            val dataStoreKey = longPreferencesKey(key)
            preferences[dataStoreKey] = time
        }
    }

    override suspend fun getLastFetchTime(key: String): Long {
        return try {
            val dataStoreKey = longPreferencesKey(key)
            dataStore.data.first()[dataStoreKey] ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}