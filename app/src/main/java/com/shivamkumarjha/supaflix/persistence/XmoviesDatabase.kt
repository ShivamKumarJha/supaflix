package com.shivamkumarjha.supaflix.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shivamkumarjha.supaflix.model.xmovies.Home

@Database(
    entities = [
        Home::class
    ],
    version = 1
)

@TypeConverters(XmoviesTypeConverter::class)
abstract class XmoviesDatabase : RoomDatabase() {
    abstract fun xmoviesDao(): XmoviesDao
}