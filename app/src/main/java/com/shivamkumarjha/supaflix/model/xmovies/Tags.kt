package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tags(
    @Json(name = "name") val name: String,
    @Json(name = "slug") val slug: String
)