package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Covers(
    @Json(name = "contentHash") val contentHash: String,
    @Json(name = "name") val name: String,
    @Json(name = "released") val released: Int,
    @Json(name = "slug") val slug: String,
    @Json(name = "description") val description: String,
    @Json(name = "coverUrl") val coverUrl: String
)