package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "status") val status: String,
    @Json(name = "results") val results: List<Contents>,
    @Json(name = "total_count") val total_count: Int,
    @Json(name = "per_page") val per_page: Int,
    @Json(name = "current_page") val current_page: Int
)