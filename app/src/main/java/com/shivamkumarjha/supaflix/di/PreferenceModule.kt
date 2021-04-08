package com.shivamkumarjha.supaflix.di

import android.content.Context
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    @Singleton
    fun getPreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)
}