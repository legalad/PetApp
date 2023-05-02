package com.example.petapp.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.android.datastore.UserPreferences
import com.example.petapp.data.DefaultUserSettingsDataRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import java.io.OutputStream


@Module
@InstallIn(SingletonComponent::class)
class ProtoDataStoreModule {
    @Suppress("BlockingMethodInNonBlockingContext")
    object UserSettingsSerializer : Serializer<UserPreferences> {
        override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): UserPreferences {
            try {
                return UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            } catch (e: java.io.IOException) {
                e.printStackTrace()
                throw e
            }
        }

        override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
    }

    private val Context.userSettingsDataStore: DataStore<UserPreferences> by dataStore(
        fileName = "UserSettings.pb",
        serializer = UserSettingsSerializer
    )

    @Provides
    @Reusable
    fun provideProtoDataStore(@ApplicationContext context: Context) =
        context.userSettingsDataStore

    @Provides
    @Reusable
    internal fun providesUserSettingsDataRepository(
        @ApplicationContext context: Context,
        userSettingsDataStore: DataStore<UserPreferences>
    ): UserSettingsDataRepository {
        return DefaultUserSettingsDataRepository(
            context,
            userSettingsDataStore
        )
    }
}