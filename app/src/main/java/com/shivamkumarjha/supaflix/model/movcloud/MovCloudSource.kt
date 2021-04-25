package com.shivamkumarjha.supaflix.model.movcloud

import com.google.gson.annotations.SerializedName


data class MovCloudSource(
    @SerializedName("file") val file: String,
    @SerializedName("resolution") val resolution: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("label") val label: String
)