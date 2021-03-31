package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    @Json(name = "status") val status: String,
    @Json(name = "meta") val meta: Meta,
    @Json(name = "h1Text") val h1Text: String,
    @Json(name = "description") val description: String,
    @Json(name = "contentsPerPage") val contentsPerPage: Int,
    @Json(name = "contentList") val contentList: List<Contents>,
    @Json(name = "totalCount") val totalCount: Int,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "canonicalUrl") val canonicalUrl: String
)