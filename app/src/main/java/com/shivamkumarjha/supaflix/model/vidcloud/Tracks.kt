package com.shivamkumarjha.supaflix.model.vidcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracks(
    @Json(name = "file") val file: String,
    @Json(name = "label") val label: String,
    @Json(name = "kind") val kind: String,
    @Json(name = "default") val default: Boolean
)