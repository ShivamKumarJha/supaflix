package com.shivamkumarjha.supaflix.ui.dashboard

import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.ui.search.SearchType

sealed class DashboardInteractionEvents {
    data class OpenMovieDetail(val hash: String) : DashboardInteractionEvents()
    data class SearchMovie(val keyWord: String) : DashboardInteractionEvents()
    data class SearchYear(val year: String) : DashboardInteractionEvents()
    data class SearchGenre(val property: Property) : DashboardInteractionEvents()
    data class ResumePlayback(val history: History) : DashboardInteractionEvents()
    data class ShortSearch(val searchType: SearchType) : DashboardInteractionEvents()
}