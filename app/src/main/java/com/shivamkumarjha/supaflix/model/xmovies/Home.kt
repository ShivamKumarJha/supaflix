package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Home(
    @Json(name = "status") val status: String,
    @Json(name = "meta") val meta: Meta,
    @Json(name = "h1Text") val h1Text: String,
    @Json(name = "description") val description: String,
    @Json(name = "sliderEnabled") val sliderEnabled: Boolean,
    @Json(name = "sliderMaxSlides") val sliderMaxSlides: Int,
    @Json(name = "sectionFeaturedEnabled") val sectionFeaturedEnabled: Boolean,
    @Json(name = "sectionFeaturedMaxContents") val sectionFeaturedMaxContents: Int,
    @Json(name = "sectionMoviesEnabled") val sectionMoviesEnabled: Boolean,
    @Json(name = "sectionMoviesMaxContents") val sectionMoviesMaxContents: Int,
    @Json(name = "sectionSeriesEnabled") val sectionSeriesEnabled: Boolean,
    @Json(name = "sectionSeriesMaxContents") val sectionSeriesMaxContents: Int,
    @Json(name = "sectionFeaturedTitle") val sectionFeaturedTitle: String,
    @Json(name = "sectionMoviesTitle") val sectionMoviesTitle: String,
    @Json(name = "sectionSeriesTitle") val sectionSeriesTitle: String,
    @Json(name = "covers") val covers: List<Covers>,
    @Json(name = "series") val series: List<Contents>,
    @Json(name = "movies") val movies: List<Contents>,
    @Json(name = "featured") val featured: List<Contents>,
    @Json(name = "canonicalUrl") val canonicalUrl: String
)