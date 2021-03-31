package com.shivamkumarjha.supaflix.model.movcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovCloud(
    @Json(name = "success") val success: Boolean,
    @Json(name = "data") val data: MovCloudData
)