package com.shivamkumarjha.supaflix.model.xmovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "content")

data class Content(
    @PrimaryKey @SerializedName("hash") val hash: String,
    @SerializedName("name") val name: String,
    @SerializedName("duration") val duration: String?,
    @SerializedName("released") val released: String?,
    @SerializedName("slug") val slug: String,
    @SerializedName("quality") val quality: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("imdb_rating") val imdb_rating: String?,
    @SerializedName("type") val type: Int,
    @SerializedName("actors") val actors: List<Property>,
    @SerializedName("directors") val directors: List<Property>,
    @SerializedName("genres") val genres: List<Property>,
    @SerializedName("countries") val countries: List<Property>,
    @SerializedName("tags") val tags: List<Tags>,
    @SerializedName("similarContents") val similarContents: List<SimilarContents>,
    @SerializedName("description") val description: String?,
    @SerializedName("review") val review: String,
    @SerializedName("trailer") val trailer: String?,
    @SerializedName("backdrop_url") val backdrop_url: String?,
    @SerializedName("episodes") val episodes: List<Episode>,
    @SerializedName("imdbId") val imdbId: String?,
    @SerializedName("tmdbId") val tmdbId: Int?
)