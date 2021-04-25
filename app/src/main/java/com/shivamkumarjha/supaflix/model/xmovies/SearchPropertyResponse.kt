package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class SearchPropertyResponse(
    @SerializedName("status") val status: String,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("h1Text") val h1Text: String,
    @SerializedName("description") val description: String,
    @SerializedName("contentsPerPage") val contentsPerPage: Int,
    @SerializedName("contentList") val contentList: List<Contents>,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("name") val name: String,
    @SerializedName("review") val review: String,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("hash") val hash: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("canonicalUrl") val canonicalUrl: String
)