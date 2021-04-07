package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPropertyResponse(
    @Json(name = "status") val status: String,
    @Json(name = "meta") val meta: Meta,
    @Json(name = "h1Text") val h1Text: String,
    @Json(name = "description") val description: String,
    @Json(name = "contentsPerPage") val contentsPerPage: Int,
    @Json(name = "contentList") val contentList: List<Contents>,
    @Json(name = "totalCount") val totalCount: Int,
    @Json(name = "name") val name: String,
    @Json(name = "review") val review: String,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "hash") val hash: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "canonicalUrl") val canonicalUrl: String
)