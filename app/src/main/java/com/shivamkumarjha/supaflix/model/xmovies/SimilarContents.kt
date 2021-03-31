package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarContents(
    @Json(name = "hash") val hash: String,
    @Json(name = "name") val name: String,
    @Json(name = "released") val released: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "poster_path") val poster_path: String,
    @Json(name = "quality") val quality: String,
    @Json(name = "type") val type: Int,
    @Json(name = "duration") val duration: String,
    @Json(name = "views") val views: Int,
    @Json(name = "episode_count") val episode_count: Int
)