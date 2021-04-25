package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class SimilarContents(
    @SerializedName("hash") val hash: String,
    @SerializedName("name") val name: String,
    @SerializedName("released") val released: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("quality") val quality: String,
    @SerializedName("type") val type: Int,
    @SerializedName("duration") val duration: String,
    @SerializedName("views") val views: Int,
    @SerializedName("episode_count") val episode_count: Int
)