package com.shivamkumarjha.supaflix.model.gocdn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GocdnSource(
    @Json(name = "file") val file: String,
    @Json(name = "label") val label: String,
    @Json(name = "type") val type: String
)