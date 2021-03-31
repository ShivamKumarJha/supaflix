package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Query(
    @Json(name = "genres") val genres: String = "all",
    @Json(name = "countries") val countries: String = "all",
    @Json(name = "years") val years: String = "all",
    @Json(name = "sort") val sort: String = "latest",
    @Json(name = "type") val type: String = "all"
)