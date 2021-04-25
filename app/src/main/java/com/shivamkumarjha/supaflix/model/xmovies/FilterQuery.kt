package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class FilterQuery(
    @SerializedName("genres") val genres: String = "all",
    @SerializedName("countries") val countries: String = "all",
    @SerializedName("years") val years: String = "all",
    @SerializedName("sort") val sort: String = "latest",
    @SerializedName("type") val type: String = "all"
)