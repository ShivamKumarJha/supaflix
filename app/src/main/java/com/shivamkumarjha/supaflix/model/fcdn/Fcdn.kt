package com.shivamkumarjha.supaflix.model.fcdn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fcdn(
    @Json(name = "success") val success: Boolean,
    @Json(name = "data") val data: List<FcdnData>,
)