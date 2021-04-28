package com.shivamkumarjha.supaflix.ui.player

import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource

sealed class PlayerInteractionEvents {
    data class NavigateUp(val isError: Boolean) : PlayerInteractionEvents()
    data class ToggleOrientation(val orientation: Int) : PlayerInteractionEvents()
    data class OpenBrowser(val url: String) : PlayerInteractionEvents()
    data class OpenPlayer(val videoPlayerSource: VideoPlayerSource) : PlayerInteractionEvents()
}