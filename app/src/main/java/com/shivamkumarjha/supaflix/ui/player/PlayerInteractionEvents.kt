package com.shivamkumarjha.supaflix.ui.player

sealed class PlayerInteractionEvents {
    object NavigateUp : PlayerInteractionEvents()
    data class OpenBrowser(val url: String) : PlayerInteractionEvents()
    data class OpenPlayer(val url: String, val type: String, val subtitle: String?) :
        PlayerInteractionEvents()
}