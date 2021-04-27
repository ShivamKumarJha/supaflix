package com.shivamkumarjha.supaflix.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shivamkumarjha.supaflix.model.db.DbContents
import com.shivamkumarjha.supaflix.model.db.DbHome
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Content

@Database(
    entities = [
        Content::class,
        DbContents::class,
        DbHome::class,
        Favourite::class,
        History::class,
    ],
    version = 1
)

@TypeConverters(XmoviesTypeConverter::class)
abstract class SupaflixDatabase : RoomDatabase() {
    abstract fun xmoviesDao(): XmoviesDao
}