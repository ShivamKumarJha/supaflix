package com.shivamkumarjha.supaflix.model.movcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovCloudData(
    @Json(name = "sources") val sources: List<MovCloudSource>,
    @Json(name = "name") val name: String
)