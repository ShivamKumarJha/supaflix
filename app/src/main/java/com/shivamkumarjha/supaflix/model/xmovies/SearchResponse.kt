package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("results") val results: List<Contents>,
    @SerializedName("total_count") val total_count: Int,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("current_page") val current_page: Int
)