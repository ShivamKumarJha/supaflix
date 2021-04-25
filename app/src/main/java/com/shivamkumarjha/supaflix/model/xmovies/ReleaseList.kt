package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class ReleaseList(
    @SerializedName("status") val status: String,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("h1Text") val h1Text: String,
    @SerializedName("description") val description: String,
    @SerializedName("years") val years: List<Int>,
    @SerializedName("canonicalUrl") val canonicalUrl: String
)