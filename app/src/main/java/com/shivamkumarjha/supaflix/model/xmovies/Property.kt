package com.shivamkumarjha.supaflix.model.xmovies

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    @Json(name = "name") val name: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "hash") val hash: String
) : Parcelable