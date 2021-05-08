package com.shivamkumarjha.supaflix.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.persistence.SupaflixDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `download` (`downloadID` INTEGER NOT NULL, `url` TEXT NOT NULL, `type` TEXT NOT NULL, `history` TEXT NOT NULL, `filePath` TEXT, PRIMARY KEY(`downloadID`))")
        }
    }

    @Provides
    @Singleton
    fun supaflixDatabase(@ApplicationContext context: Context): SupaflixDatabase =
        Room.databaseBuilder(context, SupaflixDatabase::class.java, Constants.DB_SUPAFLIX)
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun xmoviesDao(supaflixDatabase: SupaflixDatabase) = supaflixDatabase.xmoviesDao()
}