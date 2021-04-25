package com.shivamkumarjha.supaflix.model.gocdn

import com.google.gson.annotations.SerializedName


data class Gocdn(
    @SerializedName("status") val status: String,
    @SerializedName("sources") val sources: List<GocdnSource>,
    //@Json(name ="subtitles") val subtitles: List<String>
)