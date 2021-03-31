package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Watch(
    @Json(name = "status") val status: String,
    @Json(name = "meta") val meta: Meta,
    @Json(name = "similarTitle") val similarTitle: String,
    @Json(name = "schemaEnabled") val schemaEnabled: Boolean,
    @Json(name = "commentsEnabled") val commentsEnabled: Boolean,
    @Json(name = "premiumAlertEnabled") val premiumAlertEnabled: Boolean,
    @Json(name = "premiumAlertText") val premiumAlertText: String,
    @Json(name = "premiumAlertButtonText") val premiumAlertButtonText: String,
    @Json(name = "h1Text") val h1Text: String,
    @Json(name = "content") val content: Content,
    @Json(name = "buttonsPosition") val buttonsPosition: Int,
    @Json(name = "streamBtnText") val streamBtnText: String,
    @Json(name = "downloadBtnText") val downloadBtnText: String,
    @Json(name = "canonicalUrl") val canonicalUrl: String
)