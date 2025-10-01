package com.f8fit.bambutestandroid.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

    val Context.dataStore by preferencesDataStore(name = "profile_prefs")

    object ProfilePreferences {
        private val PROFILE_URI = stringPreferencesKey("profile_uri")

        suspend fun saveProfileUri(context: Context, uri: String) {
            context.dataStore.edit { prefs ->
                prefs[PROFILE_URI] = uri
            }
        }

        fun getProfileUri(context: Context): Flow<String?> {
            return context.dataStore.data.map { prefs ->
                prefs[PROFILE_URI]
            }
        }
    }

