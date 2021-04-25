package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Contents(
    @SerializedName("hash") val hash: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("released") val released: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("type") val type: Int,
    @SerializedName("quality") val quality: String,
    @SerializedName("episode_count") val episode_count: Int
)