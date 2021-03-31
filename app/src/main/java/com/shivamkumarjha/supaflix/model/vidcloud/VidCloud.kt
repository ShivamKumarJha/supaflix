package com.shivamkumarjha.supaflix.model.vidcloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VidCloud(
    @Json(name = "source") val source: List<VidCloudSource>?,
    //@Json(name ="source_bk") val source_bk: List<VidCloudSource>,
    @Json(name = "track") val track: Track?,
    @Json(name = "linkiframe") val linkiframe: String?
)