package com.shivamkumarjha.supaflix.persistence

import androidx.room.TypeConverter
import com.shivamkumarjha.supaflix.model.xmovies.*
import com.squareup.moshi.Moshi

class XmoviesTypeConverter {
    companion object {
        private val moshi = Moshi.Builder().build()

        @TypeConverter
        @JvmStatic
        fun stringToHome(home: String): Home? {
            return moshi.adapter(Home::class.java).fromJson(home)
        }

        @TypeConverter
        @JvmStatic
        fun homeToString(home: Home): String {
            return moshi.adapter(Home::class.java).toJson(home)
        }

        @TypeConverter
        @JvmStatic
        fun stringToContentsResponse(contentsResponse: String): ContentsResponse? {
            return moshi.adapter(ContentsResponse::class.java).fromJson(contentsResponse)
        }

        @TypeConverter
        @JvmStatic
        fun contentsResponseToString(contentsResponse: ContentsResponse): String {
            return moshi.adapter(ContentsResponse::class.java).toJson(contentsResponse)
        }

        @TypeConverter
        @JvmStatic
        fun stringToActors(actors: String): List<Actors>? {
            return moshi.adapter<List<Actors>>(Actors::class.java).fromJson(actors)
        }

        @TypeConverter
        @JvmStatic
        fun actorsToString(actors: List<Actors>): String {
            return moshi.adapter<List<Actors>>(Actors::class.java).toJson(actors)
        }

        @TypeConverter
        @JvmStatic
        fun stringToDirectors(directors: String): List<Directors>? {
            return moshi.adapter<List<Directors>>(Directors::class.java).fromJson(directors)
        }

        @TypeConverter
        @JvmStatic
        fun directorsToString(directors: List<Directors>): String {
            return moshi.adapter<List<Directors>>(Directors::class.java).toJson(directors)
        }

        @TypeConverter
        @JvmStatic
        fun stringToGenres(genres: String): List<Genres>? {
            return moshi.adapter<List<Genres>>(Genres::class.java).fromJson(genres)
        }

        @TypeConverter
        @JvmStatic
        fun genresToString(genres: List<Genres>): String {
            return moshi.adapter<List<Genres>>(Genres::class.java).toJson(genres)
        }

        @TypeConverter
        @JvmStatic
        fun stringToCountries(countries: String): List<Countries>? {
            return moshi.adapter<List<Countries>>(Countries::class.java).fromJson(countries)
        }

        @TypeConverter
        @JvmStatic
        fun countriesToString(countries: List<Countries>): String {
            return moshi.adapter<List<Countries>>(Countries::class.java).toJson(countries)
        }

        @TypeConverter
        @JvmStatic
        fun stringToTags(tags: String): List<Tags>? {
            return moshi.adapter<List<Tags>>(Tags::class.java).fromJson(tags)
        }

        @TypeConverter
        @JvmStatic
        fun tagsToString(tags: List<Tags>): String {
            return moshi.adapter<List<Tags>>(Tags::class.java).toJson(tags)
        }

        @TypeConverter
        @JvmStatic
        fun stringToSimilarContents(similarContents: String): List<SimilarContents>? {
            return moshi.adapter<List<SimilarContents>>(SimilarContents::class.java)
                .fromJson(similarContents)
        }

        @TypeConverter
        @JvmStatic
        fun similarContentsToString(similarContents: List<SimilarContents>): String {
            return moshi.adapter<List<SimilarContents>>(SimilarContents::class.java)
                .toJson(similarContents)
        }

        @TypeConverter
        @JvmStatic
        fun stringToEpisodes(episodes: String): List<Episodes>? {
            return moshi.adapter<List<Episodes>>(Episodes::class.java).fromJson(episodes)
        }

        @TypeConverter
        @JvmStatic
        fun episodesToString(episodes: List<Episodes>): String {
            return moshi.adapter<List<Episodes>>(Episodes::class.java).toJson(episodes)
        }
    }
}