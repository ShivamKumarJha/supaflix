package com.shivamkumarjha.supaflix.ui.player

import com.shivamkumarjha.supaflix.model.db.History

sealed class PlayerInteractionEvents {
    object NavigateUp : PlayerInteractionEvents()
    data class OpenBrowser(val url: String) : PlayerInteractionEvents()
    data class OpenPlayer(
        val url: String,
        val type: String,
        val subtitle: String?,
        val history: History,
        val viewModel: PlayerViewModel
    ) : PlayerInteractionEvents()
}