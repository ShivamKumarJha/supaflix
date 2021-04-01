package com.shivamkumarjha.supaflix.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shivamkumarjha.supaflix.model.db.DbContents
import com.shivamkumarjha.supaflix.model.db.DbHome

@Database(
    entities = [
        DbContents::class,
        DbHome::class,
    ],
    version = 1
)

@TypeConverters(XmoviesTypeConverter::class)
abstract class XmoviesDatabase : RoomDatabase() {
    abstract fun xmoviesDao(): XmoviesDao
}