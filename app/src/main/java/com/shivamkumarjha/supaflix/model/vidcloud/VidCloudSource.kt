package com.shivamkumarjha.supaflix.model.vidcloud

import com.google.gson.annotations.SerializedName


data class VidCloudSource(
    @SerializedName("file") val file: String,
    @SerializedName("label") val label: String,
    @SerializedName("default") val default: Boolean,
    @SerializedName("type") val type: String
)