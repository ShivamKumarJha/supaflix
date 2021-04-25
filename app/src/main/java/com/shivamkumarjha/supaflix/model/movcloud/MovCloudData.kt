package com.shivamkumarjha.supaflix.model.movcloud

import com.google.gson.annotations.SerializedName


data class MovCloudData(
    @SerializedName("sources") val sources: List<MovCloudSource>,
    @SerializedName("name") val name: String
)