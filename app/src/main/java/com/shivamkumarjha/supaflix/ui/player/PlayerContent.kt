package com.shivamkumarjha.supaflix.ui.player

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.History
import java.util.*

@Composable
fun PlayerContent(
    url: String,
    type: String,
    subtitleUrl: String? = null,
    history: History,
    viewModel: PlayerViewModel
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    var autoPlay = true
    var window = history.window
    var position = history.position

    val exoPlayer = remember {
        val player = SimpleExoPlayer.Builder(context).build().apply {
            val dataSourceFactory = DefaultDataSourceFactory(context, "vidcloud9.com")
            val mediaSource = if (type.toLowerCase(Locale.ROOT).contains("dash") ||
                type.toLowerCase(Locale.ROOT).contains("m3u8") ||
                type.toLowerCase(Locale.ROOT).contains("hls")
            ) {
                HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
            } else {
                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
            }
            if (subtitleUrl != null) {
                val mimeTypes = if (subtitleUrl.contains(".vtt"))
                    MimeTypes.TEXT_VTT
                else
                    MimeTypes.APPLICATION_SUBRIP
                val textFormat = Format.createTextSampleFormat(
                    null,
                    mimeTypes,
                    null,
                    Format.NO_VALUE,
                    Format.NO_VALUE,
                    "en",
                    null,
                    Format.OFFSET_SAMPLE_RELATIVE
                )
                val textMediaSource: MediaSource =
                    SingleSampleMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(subtitleUrl), textFormat, C.TIME_UNSET)
                val mergedSource = MergingMediaSource(mediaSource, textMediaSource)
                this.prepare(mergedSource)
            } else {
                this.prepare(mediaSource)
            }
        }
        player.playWhenReady = autoPlay
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.seekTo(window, position)
        player
    }

    fun updateState() {
        autoPlay = exoPlayer.playWhenReady
        window = exoPlayer.currentWindowIndex
        position = 0L.coerceAtLeast(exoPlayer.contentPosition)
        history.window = window
        history.position = position
        viewModel.updateHistory(history)
    }

    val playerView = remember {
        val playerView = PlayerView(context)
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                playerView.onResume()
                exoPlayer.playWhenReady = autoPlay
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                updateState()
                playerView.onPause()
                exoPlayer.playWhenReady = false
            }
        })
        playerView
    }

    DisposableEffect(key1 = url) {
        onDispose {
            updateState()
            exoPlayer.release()
        }
    }

    AndroidView(
        { playerView }, modifier = Modifier
            .fillMaxSize()
    ) {
        playerView.player = exoPlayer
        playerView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
    }
}