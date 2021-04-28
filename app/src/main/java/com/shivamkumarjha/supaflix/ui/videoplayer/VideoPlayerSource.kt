package com.shivamkumarjha.supaflix.ui.videoplayer

import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.ui.player.PlayerInteractionEvents
import com.shivamkumarjha.supaflix.ui.player.PlayerViewModel

data class VideoPlayerSource(
    val url: String,
    val type: String,
    var subtitleUrl: String?,
    val history: History,
    val viewModel: PlayerViewModel,
    val interactionEvents: (PlayerInteractionEvents) -> Unit
)