package com.shivamkumarjha.supaflix.utility

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.shivamkumarjha.supaflix.ui.videoplayer.ExoPlayerCache
import java.util.*

object ExoPlayer {

    private fun getDataSourceFactory(context: Context): DefaultHttpDataSourceFactory {
        return DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, context.packageName),
            60000,
            60000,
            true
        )
    }

    fun getMediaSource(url: String, context: Context, referer: String? = null): MediaSource {
        val dataSourceFactory = getDataSourceFactory(context)
        val cacheFactory = CacheDataSourceFactory(
            ExoPlayerCache.getInstance(context),
            dataSourceFactory,
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )

        if (referer != null) {
            val headers: MutableMap<String, String> = HashMap()
            headers["Referer"] = referer
            dataSourceFactory.defaultRequestProperties.set(headers)
        }

        return when (Util.inferContentType(Uri.parse(url))) {
            C.TYPE_HLS -> HlsMediaSource.Factory(cacheFactory)
                .createMediaSource(Uri.parse(url))
            C.TYPE_SS -> SsMediaSource.Factory(cacheFactory)
                .createMediaSource(Uri.parse(url))
            C.TYPE_DASH -> DashMediaSource.Factory(cacheFactory)
                .createMediaSource(Uri.parse(url))
            else -> ProgressiveMediaSource.Factory(cacheFactory)
                .createMediaSource(Uri.parse(url))
        }
    }
}