package com.shivamkumarjha.supaflix.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayer
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource
import com.shivamkumarjha.supaflix.ui.videoplayer.rememberVideoPlayerController

@Composable
fun PlayerContent(videoPlayerSource: VideoPlayerSource) {
    val videoPlayerController = rememberVideoPlayerController(videoPlayerSource)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(videoPlayerController, lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                videoPlayerController.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    VideoPlayer(videoPlayerSource, videoPlayerController)
}