package com.shivamkumarjha.supaflix.model.gocdn

import com.google.gson.annotations.SerializedName


data class GocdnSource(
    @SerializedName("file") val file: String,
    @SerializedName("label") val label: String,
    @SerializedName("type") val type: String
)