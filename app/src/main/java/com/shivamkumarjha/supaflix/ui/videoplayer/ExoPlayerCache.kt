package com.shivamkumarjha.supaflix.ui.videoplayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.DefaultDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


object ExoPlayerCache {

    private var cache: SimpleCache? = null
    private const val maxSize: Long = 50 * 1024 * 1024

    fun getInstance(context: Context): SimpleCache {
        val dp: DatabaseProvider = DefaultDatabaseProvider(
            object : SQLiteOpenHelper(
                context,
                "ExoPlayer",
                null,
                1
            ) {
                override fun onCreate(db: SQLiteDatabase) {}
                override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            })
        if (cache == null) {
            cache = SimpleCache(
                File(
                    context.cacheDir,
                    "ExoPlayerCache"
                ),
                LeastRecentlyUsedCacheEvictor(maxSize),
                dp
            )
        }
        return cache!!
    }
}