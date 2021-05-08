package com.shivamkumarjha.supaflix.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.*

class XmoviesTypeConverter {
    companion object {

        @TypeConverter
        @JvmStatic
        fun stringToHistory(history: String): History {
            val type = object : TypeToken<History>() {}.type
            return Gson().fromJson(history, type)
        }

        @TypeConverter
        @JvmStatic
        fun historyToJson(history: History): String {
            val type = object : TypeToken<History>() {}.type
            return Gson().toJson(history, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToHome(home: String): Home {
            val type = object : TypeToken<Home>() {}.type
            return Gson().fromJson(home, type)
        }

        @TypeConverter
        @JvmStatic
        fun homeToJson(home: Home): String {
            val type = object : TypeToken<Home>() {}.type
            return Gson().toJson(home, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToContentsResponse(contentsResponse: String): ContentsResponse {
            val type = object : TypeToken<ContentsResponse>() {}.type
            return Gson().fromJson(contentsResponse, type)
        }

        @TypeConverter
        @JvmStatic
        fun contentsResponseToJson(contentsResponse: ContentsResponse): String {
            val type = object : TypeToken<ContentsResponse>() {}.type
            return Gson().toJson(contentsResponse, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToProperty(property: String): List<Property> {
            val type = object : TypeToken<List<Property>>() {}.type
            return Gson().fromJson(property, type)
        }

        @TypeConverter
        @JvmStatic
        fun propertyToJson(property: List<Property>): String {
            val type = object : TypeToken<List<Property>>() {}.type
            return Gson().toJson(property, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToTags(tags: String): List<Tags> {
            val type = object : TypeToken<List<Tags>>() {}.type
            return Gson().fromJson(tags, type)
        }

        @TypeConverter
        @JvmStatic
        fun tagsToJson(tags: List<Tags>): String {
            val type = object : TypeToken<List<Tags>>() {}.type
            return Gson().toJson(tags, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToSimilarContents(similarContents: String): List<SimilarContents> {
            val type = object : TypeToken<List<SimilarContents>>() {}.type
            return Gson().fromJson(similarContents, type)
        }

        @TypeConverter
        @JvmStatic
        fun similarContentsToJson(similarContents: List<SimilarContents>): String {
            val type = object : TypeToken<List<SimilarContents>>() {}.type
            return Gson().toJson(similarContents, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToEpisodes(episodes: String): List<Episode> {
            val type = object : TypeToken<List<Episode>>() {}.type
            return Gson().fromJson(episodes, type)
        }

        @TypeConverter
        @JvmStatic
        fun episodesToJson(episodes: List<Episode>): String {
            val type = object : TypeToken<List<Episode>>() {}.type
            return Gson().toJson(episodes, type)
        }
    }
}