package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Watch(
    @SerializedName("status") val status: String,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("similarTitle") val similarTitle: String,
    @SerializedName("schemaEnabled") val schemaEnabled: Boolean,
    @SerializedName("commentsEnabled") val commentsEnabled: Boolean,
    @SerializedName("premiumAlertEnabled") val premiumAlertEnabled: Boolean,
    @SerializedName("premiumAlertText") val premiumAlertText: String,
    @SerializedName("premiumAlertButtonText") val premiumAlertButtonText: String,
    @SerializedName("h1Text") val h1Text: String,
    @SerializedName("content") val content: Content,
    @SerializedName("buttonsPosition") val buttonsPosition: Int,
    @SerializedName("streamBtnText") val streamBtnText: String,
    @SerializedName("downloadBtnText") val downloadBtnText: String,
    @SerializedName("canonicalUrl") val canonicalUrl: String
)