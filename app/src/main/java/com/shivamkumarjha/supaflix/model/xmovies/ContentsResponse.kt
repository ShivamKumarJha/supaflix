package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentsResponse(
    @Json(name = "status") val status: String,
    @Json(name = "contents") val contents: List<Contents>
)