package com.example.petapp.di

import android.content.Context
import com.example.petapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CameraFile

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CameraExecutor

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Singleton
    @Provides
    @CameraFile
    fun provideOutputDirectory(
        @ApplicationContext applicationContext: Context
    ): File {
        val mediaDir = applicationContext.externalMediaDirs.firstOrNull()?.let {
            File(it, applicationContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else applicationContext.filesDir
    }
}