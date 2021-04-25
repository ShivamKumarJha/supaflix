package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Embeds(
    @SerializedName("hash") val hash: String,
    @SerializedName("server") val server: String,
    @SerializedName("part_of") val part_of: String
)