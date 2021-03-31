package com.shivamkumarjha.supaflix.model.vidcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Track(@Json(name = "tracks") val tracks: ArrayList<Tracks>)