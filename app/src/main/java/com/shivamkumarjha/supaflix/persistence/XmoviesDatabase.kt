package com.shivamkumarjha.supaflix.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivamkumarjha.supaflix.model.xmovies.Home

@Database(
    entities = [
        Home::class
    ],
    version = 1
)

abstract class XmoviesDatabase : RoomDatabase() {
    abstract fun xmoviesDao(): XmoviesDao
}