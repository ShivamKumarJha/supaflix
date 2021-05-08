package com.shivamkumarjha.supaflix.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shivamkumarjha.supaflix.model.db.*
import com.shivamkumarjha.supaflix.model.xmovies.Content

@Database(
    entities = [
        Content::class,
        DbContents::class,
        DbHome::class,
        Download::class,
        Favourite::class,
        History::class,
    ],
    version = 2
)

@TypeConverters(XmoviesTypeConverter::class)
abstract class SupaflixDatabase : RoomDatabase() {
    abstract fun xmoviesDao(): XmoviesDao
}