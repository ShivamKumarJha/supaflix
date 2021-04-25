package com.shivamkumarjha.supaflix.model.fcdn

import com.google.gson.annotations.SerializedName


data class Fcdn(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<FcdnData>,
)