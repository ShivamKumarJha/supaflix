package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseList(
    @Json(name = "status") val status: String,
    @Json(name = "meta") val meta: Meta,
    @Json(name = "h1Text") val h1Text: String,
    @Json(name = "description") val description: String,
    @Json(name = "years") val years: List<Int>,
    @Json(name = "canonicalUrl") val canonicalUrl: String
)