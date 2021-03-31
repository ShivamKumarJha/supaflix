package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerResponse(
    @Json(name = "status") val status: String,
    @Json(name = "url") val url: String,
    @Json(name = "subtitles") val subtitles: String,
    @Json(name = "server") val server: Int,
    @Json(name = "big_player") val big_player: Int
)