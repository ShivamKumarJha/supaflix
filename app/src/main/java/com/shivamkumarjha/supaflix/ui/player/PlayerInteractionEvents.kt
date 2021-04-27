package com.shivamkumarjha.supaflix.ui.player

import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource

sealed class PlayerInteractionEvents {
    object NavigateUp : PlayerInteractionEvents()
    data class OpenBrowser(val url: String) : PlayerInteractionEvents()
    data class OpenPlayer(val videoPlayerSource: VideoPlayerSource) : PlayerInteractionEvents()
}