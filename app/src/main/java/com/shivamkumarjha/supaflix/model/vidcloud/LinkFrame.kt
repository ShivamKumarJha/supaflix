package com.shivamkumarjha.supaflix.model.vidcloud

import com.google.gson.annotations.SerializedName


data class LinkFrame(
    @SerializedName("source") val source: List<VidCloudSource>,
    @SerializedName("linkiframe") val linkiframe: String?
)