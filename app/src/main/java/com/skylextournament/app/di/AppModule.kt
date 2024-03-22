package com.skylextournament.app.di

import android.content.Context
import com.skylextournament.app.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSessionRepository(@ApplicationContext context: Context): SessionRepository =
        SessionRepository(context)
}