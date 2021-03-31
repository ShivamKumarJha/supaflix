package com.shivamkumarjha.supaflix.model.movcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovCloudSource(
    @Json(name = "file") val file: String,
    @Json(name = "resolution") val resolution: String,
    @Json(name = "height") val height: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "label") val label: String
)