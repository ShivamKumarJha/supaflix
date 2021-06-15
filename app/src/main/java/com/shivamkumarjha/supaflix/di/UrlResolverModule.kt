package com.shivamkumarjha.supaflix.di

import android.content.Context
import com.shivamkumarjha.supaflix.utility.UrlResolver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UrlResolverModule {

    @Provides
    @Singleton
    fun getUrlResolver(@ApplicationContext context: Context) = UrlResolver(context)
}