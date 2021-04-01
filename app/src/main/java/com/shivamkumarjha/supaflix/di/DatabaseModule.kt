package com.shivamkumarjha.supaflix.di

import android.content.Context
import androidx.room.Room
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.persistence.XmoviesDatabase
import com.shivamkumarjha.supaflix.persistence.XmoviesTypeConverter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun xmoviesDatabase(@ApplicationContext context: Context, moshi: Moshi): XmoviesDatabase =
        Room.databaseBuilder(context, XmoviesDatabase::class.java, Constants.DB_XMOVIES)
            .addTypeConverter(XmoviesTypeConverter(moshi))
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun xmoviesDao(xmoviesDatabase: XmoviesDatabase) = xmoviesDatabase.xmoviesDao()
}