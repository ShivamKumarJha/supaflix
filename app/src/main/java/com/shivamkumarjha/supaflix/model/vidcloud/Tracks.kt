package com.shivamkumarjha.supaflix.model.vidcloud

import com.google.gson.annotations.SerializedName


data class Tracks(
    @SerializedName("file") val file: String,
    @SerializedName("label") val label: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("default") val default: Boolean
)