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
import com.google.android.exoplayer2.util.Util
import java.util.*

object ExoPlayer {

    fun getMediaSource(url: String, context: Context, referer: String? = null): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, context.packageName),
            60000,
            60000,
            true
        )

        if (referer != null) {
            val headers: MutableMap<String, String> = HashMap()
            headers["Referer"] = referer
            dataSourceFactory.defaultRequestProperties.set(headers)
        }

        return when (Util.inferContentType(Uri.parse(url))) {
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url))
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url))
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url))
            else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url))
        }
    }
}