package com.example.scheduleapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.scheduleapp.retrofit.ScheduleService
import com.example.scheduleapp.retrofit.ScheduleServiceInstance
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.models.FirebaseImplementation
import com.example.scheduleapp.models.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRepository(fAuth: FirebaseAuth): FirebaseRepository {
        return FirebaseImplementation(fAuth)
    }

    @Provides
    @Singleton
    fun provideService(): ScheduleService {
        return ScheduleServiceInstance.createService(ScheduleService::class.java)
    }
}