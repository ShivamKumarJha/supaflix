package com.shivamkumarjha.supaflix.model.xmovies

import com.google.gson.annotations.SerializedName


data class Home(
    @SerializedName("status") val status: String,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("h1Text") val h1Text: String,
    @SerializedName("description") val description: String,
    @SerializedName("sliderEnabled") val sliderEnabled: Boolean,
    @SerializedName("sliderMaxSlides") val sliderMaxSlides: Int,
    @SerializedName("sectionFeaturedEnabled") val sectionFeaturedEnabled: Boolean,
    @SerializedName("sectionFeaturedMaxContents") val sectionFeaturedMaxContents: Int,
    @SerializedName("sectionMoviesEnabled") val sectionMoviesEnabled: Boolean,
    @SerializedName("sectionMoviesMaxContents") val sectionMoviesMaxContents: Int,
    @SerializedName("sectionSeriesEnabled") val sectionSeriesEnabled: Boolean,
    @SerializedName("sectionSeriesMaxContents") val sectionSeriesMaxContents: Int,
    @SerializedName("sectionFeaturedTitle") val sectionFeaturedTitle: String,
    @SerializedName("sectionMoviesTitle") val sectionMoviesTitle: String,
    @SerializedName("sectionSeriesTitle") val sectionSeriesTitle: String,
    @SerializedName("covers") val covers: List<Covers>,
    @SerializedName("series") val series: List<Contents>,
    @SerializedName("movies") val movies: List<Contents>,
    @SerializedName("featured") val featured: List<Contents>,
    @SerializedName("canonicalUrl") val canonicalUrl: String
)