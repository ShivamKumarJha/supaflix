package com.shivamkumarjha.supaflix.ui.search

import com.shivamkumarjha.supaflix.model.xmovies.Contents

sealed class SearchInteractionEvents {
    data class OpenDetail(val contents: Contents) : SearchInteractionEvents()
}