package com.shivamkumarjha.supaflix.model.vidcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkFrame(
    @Json(name = "source") val source: List<VidCloudSource>,
    @Json(name = "linkiframe") val linkiframe: String?
)