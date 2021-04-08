package com.shivamkumarjha.supaflix.ui.detail

import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.model.xmovies.SimilarContents

sealed class DetailInteractionEvents {
    data class ToggleFavourite(val content: Content, val status: Boolean) :
        DetailInteractionEvents()

    data class OpenEpisode(val history: History) : DetailInteractionEvents()
    data class OpenMovieDetail(val similarContents: SimilarContents) : DetailInteractionEvents()
    data class SearchGenre(val property: Property) : DetailInteractionEvents()
    data class SearchActor(val property: Property) : DetailInteractionEvents()
    data class SearchDirector(val property: Property) : DetailInteractionEvents()
    data class SearchCountry(val property: Property) : DetailInteractionEvents()
}