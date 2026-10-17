package com.example.flightsearch.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Saves the search text (and the selected airport) with Preferences DataStore,
 * so the app shows the same search after it is closed and reopened.
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
        val SELECTED_AIRPORT = stringPreferencesKey("selected_airport")
    }

    private val preferences: Flow<Preferences> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }

    val searchQuery: Flow<String> = preferences.map { it[SEARCH_QUERY] ?: "" }

    val selectedAirport: Flow<String> = preferences.map { it[SELECTED_AIRPORT] ?: "" }

    suspend fun saveSearch(query: String, selectedAirport: String) {
        dataStore.edit {
            it[SEARCH_QUERY] = query
            it[SELECTED_AIRPORT] = selectedAirport
        }
    }
}
