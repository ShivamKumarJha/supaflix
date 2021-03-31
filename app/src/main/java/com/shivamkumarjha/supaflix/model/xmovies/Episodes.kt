package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episodes(
    @Json(name = "episode_hash") val episode_hash: String,
    @Json(name = "name") val name: String,
    @Json(name = "name_sort") val name_sort: Int,
    @Json(name = "title") val title: String
)