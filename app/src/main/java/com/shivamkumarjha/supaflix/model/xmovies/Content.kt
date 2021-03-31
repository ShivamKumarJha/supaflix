package com.shivamkumarjha.supaflix.model.xmovies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "hash") val hash: String,
    @Json(name = "name") val name: String,
    @Json(name = "duration") val duration: String,
    @Json(name = "released") val released: Int,
    @Json(name = "slug") val slug: String,
    @Json(name = "quality") val quality: String,
    @Json(name = "poster_path") val poster_path: String,
    @Json(name = "imdb_rating") val imdb_rating: String,
    @Json(name = "type") val type: Int,
    @Json(name = "actors") val actors: List<Actors>,
    @Json(name = "directors") val directors: List<Directors>,
    @Json(name = "genres") val genres: List<Genres>,
    @Json(name = "countries") val countries: List<Countries>,
    @Json(name = "tags") val tags: List<Tags>,
    @Json(name = "similarContents") val similarContents: List<SimilarContents>,
    @Json(name = "description") val description: String,
    @Json(name = "review") val review: String,
    @Json(name = "trailer") val trailer: String,
    @Json(name = "backdrop_url") val backdrop_url: String,
    @Json(name = "episodes") val episodes: List<Episodes>,
    @Json(name = "imdbId") val imdbId: String,
    @Json(name = "tmdbId") val tmdbId: Int
)