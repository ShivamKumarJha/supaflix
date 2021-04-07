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
        fun stringToProperty(property: String): List<Property>? {
            return moshi.adapter<List<Property>>(Property::class.java).fromJson(property)
        }

        @TypeConverter
        @JvmStatic
        fun propertyToString(property: List<Property>): String {
            return moshi.adapter<List<Property>>(Property::class.java).toJson(property)
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