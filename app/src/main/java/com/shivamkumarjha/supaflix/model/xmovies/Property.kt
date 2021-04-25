package com.shivamkumarjha.supaflix.model.xmovies

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("hash") val hash: String
) : Parcelable