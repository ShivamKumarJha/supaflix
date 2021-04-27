package com.shivamkumarjha.supaflix.ui.videoplayer

import kotlinx.coroutines.flow.StateFlow

interface VideoPlayerController {

    fun setSource(videoPlayerSource: VideoPlayerSource)

    fun play()

    fun pause()

    fun playPauseToggle()

    fun quickSeekForward()

    fun quickSeekRewind()

    fun seekTo(position: Long)

    fun reset()

    val state: StateFlow<VideoPlayerState>
}