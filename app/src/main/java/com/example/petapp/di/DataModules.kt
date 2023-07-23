package com.example.petapp.di

import android.content.Context
import androidx.room.Room
import com.example.petapp.data.*
import com.example.petapp.data.source.local.PetAppDatabase
import com.example.petapp.data.source.local.PetDashboardLocalDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalPetsDashboardDatasource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providePetsDashboardRepository(
        @LocalPetsDashboardDatasource localDatasource: PetsDashboardDatasource,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): PetsDashboardRepository {
        return DefaultPetsDashboardRepository(
            petsDashboardLocalDatasource = localDatasource,
            ioDispatcher = ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideWorkManagerReminderRepository(
        @ApplicationContext applicationContext: Context,
        petRepository: PetsDashboardRepository,
        settingsRepository: UserSettingsDataRepository
    ): WorkManagerReminderRepository {
        return DefaultWorkManagerReminderRepository(
            context = applicationContext,
            petsDashboardRepository = petRepository,
            settingsDataRepository = settingsRepository
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatasourceModule {
    @Singleton
    @LocalPetsDashboardDatasource
    @Provides
    fun providePetsLocalDatasource(
        database: PetAppDatabase,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): PetsDashboardDatasource {
        return PetDashboardLocalDatasource(database.petsDashboardDao(), ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providePetAppDatabase(@ApplicationContext context: Context): PetAppDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = PetAppDatabase::class.java,
            name = "pet_app.db"
        ).build()
    }
}