package com.example.petapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.android.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultUserSettingsDataRepository @Inject constructor(
    private val context: Context,
    private val userSettingsDataStore: DataStore<UserPreferences>
) : UserSettingsDataRepository {
    override fun getSettings(): Flow<UserPreferences> {
        return userSettingsDataStore.data
    }

    override fun getLanguage(): Flow<UserPreferences.Language> {
        return userSettingsDataStore.data.map { it.language }
    }

    override suspend fun setLanguage(language: UserPreferences.Language) {
        userSettingsDataStore.updateData {
            it.toBuilder()
                .setLanguage(language)
                .build()
        }
    }

    override fun getUnit(): Flow<UserPreferences.Unit> {
        return userSettingsDataStore.data.map { it.unit }
    }

    override suspend fun setUnit(unit: UserPreferences.Unit) {
        userSettingsDataStore.updateData {
            it.toBuilder()
                .setUnit(unit)
                .build()
        }
    }
}