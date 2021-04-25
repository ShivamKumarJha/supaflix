package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Episodes(
    @SerializedName("episode_hash") val episode_hash: String,
    @SerializedName("name") val name: String,
    @SerializedName("name_sort") val name_sort: Int,
    @SerializedName("title") val title: String
)