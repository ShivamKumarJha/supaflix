package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmbedsResponse(
    @Json(name = "status") val status: String,
    @Json(name = "embeds") val embeds: List<Embeds>
)