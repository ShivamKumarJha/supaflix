package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Covers(
    @SerializedName("contentHash") val contentHash: String,
    @SerializedName("name") val name: String,
    @SerializedName("released") val released: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverUrl") val coverUrl: String
)