package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contents(
    @Json(name = "hash") val hash: String,
    @Json(name = "name") val name: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "released") val released: Int,
    @Json(name = "poster_path") val poster_path: String,
    @Json(name = "type") val type: Int,
    @Json(name = "quality") val quality: String,
    @Json(name = "episode_count") val episode_count: Int
)