package com.shivamkumarjha.supaflix.model.fcdn

import com.google.gson.annotations.SerializedName


data class FcdnData(
    @SerializedName("file") val file: String,
    @SerializedName("label") val label: String,
    @SerializedName("type") val type: String
)