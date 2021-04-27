package com.shivamkumarjha.supaflix.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayer
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource
import com.shivamkumarjha.supaflix.ui.videoplayer.rememberVideoPlayerController

@Composable
fun PlayerContent(
    url: String,
    type: String,
    subtitleUrl: String? = null,
    history: History,
    viewModel: PlayerViewModel
) {
    viewModel.addToHistory(history)

    val videoPlayerController = rememberVideoPlayerController()
    val videoPlayerUiState by videoPlayerController.state.collectAsState()
    videoPlayerController.setSource(VideoPlayerSource.Network(url))
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

    VideoPlayer(videoPlayerController)
}