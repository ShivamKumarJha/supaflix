package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Meta(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)