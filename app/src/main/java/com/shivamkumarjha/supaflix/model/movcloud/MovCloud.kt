package com.shivamkumarjha.supaflix.model.movcloud

import com.google.gson.annotations.SerializedName


data class MovCloud(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: MovCloudData
)