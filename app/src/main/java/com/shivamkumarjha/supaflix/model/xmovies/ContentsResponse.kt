package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class ContentsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("contents") val contents: List<Contents>
)