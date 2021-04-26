package com.shivamkumarjha.supaflix.ui.search

import com.shivamkumarjha.supaflix.model.xmovies.Contents
import com.shivamkumarjha.supaflix.ui.detail.DetailInteractionEvents

sealed class SearchInteractionEvents {
    object NavigateUp : SearchInteractionEvents()
    data class OpenDetail(val contents: Contents) : SearchInteractionEvents()
}