package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Embeds(
    @Json(name = "hash") val hash: String,
    @Json(name = "server") val server: String,
    @Json(name = "part_of") val part_of: String
)