package com.shivamkumarjha.supaflix.model.gocdn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gocdn(
    @Json(name = "status") val status: String,
    @Json(name = "sources") val sources: List<GocdnSource>,
    //@Json(name ="subtitles") val subtitles: List<String>
)