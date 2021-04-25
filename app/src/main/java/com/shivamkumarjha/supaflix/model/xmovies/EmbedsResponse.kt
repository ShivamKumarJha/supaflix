package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class EmbedsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("embeds") val embeds: List<Embeds>
)