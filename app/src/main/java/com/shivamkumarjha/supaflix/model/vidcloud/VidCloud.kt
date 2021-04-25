package com.shivamkumarjha.supaflix.model.vidcloud

import com.google.gson.annotations.SerializedName


data class VidCloud(
    @SerializedName("source") val source: List<VidCloudSource>?,
    //@Json(name ="source_bk") val source_bk: List<VidCloudSource>,
    @SerializedName("track") val track: Track?,
    @SerializedName("linkiframe") val linkiframe: String?
)