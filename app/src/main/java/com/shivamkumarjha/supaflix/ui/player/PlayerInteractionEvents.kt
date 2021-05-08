package com.shivamkumarjha.supaflix.ui.player

import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource

sealed class PlayerInteractionEvents {
    data class NavigateUp(val isError: Boolean) : PlayerInteractionEvents()
    data class ToggleOrientation(val orientation: Int) : PlayerInteractionEvents()
    data class OpenBrowser(val url: String) : PlayerInteractionEvents()
    data class OpenPlayer(val videoPlayerSource: VideoPlayerSource) : PlayerInteractionEvents()
    data class DownloadVideo(val url: String, val type: String, val history: History) :
        PlayerInteractionEvents()
}