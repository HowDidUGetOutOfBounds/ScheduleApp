package com.example.scheduleapp.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.scheduleapp.R
import com.example.scheduleapp.models.FirebaseImplementation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideDatabase(): FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.resources.getString(R.string.app_preferences), Context.MODE_PRIVATE)
    }

    @Provides
    fun provideRepository(): FirebaseImplementation {
        return FirebaseImplementation(FirebaseDatabase.getInstance(), FirebaseAuth.getInstance())
    }
}