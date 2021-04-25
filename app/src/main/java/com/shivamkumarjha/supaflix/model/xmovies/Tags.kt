package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Tags(
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)