package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class ServerResponse(
    @SerializedName("status") val status: String,
    @SerializedName("url") val url: String,
    @SerializedName("subtitles") val subtitles: String,
    @SerializedName("server") val server: Int,
    @SerializedName("big_player") val big_player: Int
)