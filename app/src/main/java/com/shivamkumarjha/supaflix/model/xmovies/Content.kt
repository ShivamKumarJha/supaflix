package com.shivamkumarjha.supaflix.model.xmovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "content")
@JsonClass(generateAdapter = true)
data class Content(
    @PrimaryKey @Json(name = "hash") val hash: String,
    @Json(name = "name") val name: String,
    @Json(name = "duration") val duration: String,
    @Json(name = "released") val released: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "quality") val quality: String,
    @Json(name = "poster_path") val poster_path: String,
    @Json(name = "imdb_rating") val imdb_rating: String,
    @Json(name = "type") val type: Int,
    @Json(name = "actors") val actors: List<Property>,
    @Json(name = "directors") val directors: List<Property>,
    @Json(name = "genres") val genres: List<Property>,
    @Json(name = "countries") val countries: List<Property>,
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