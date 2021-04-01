package com.shivamkumarjha.supaflix.persistence

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.shivamkumarjha.supaflix.model.xmovies.*
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class XmoviesTypeConverter(private val moshi: Moshi) {
    @TypeConverter
    fun stringToMeta(meta: String): Meta? {
        return moshi.adapter(Meta::class.java).fromJson(meta)
    }

    @TypeConverter
    fun metaToString(meta: Meta): String {
        return moshi.adapter(Meta::class.java).toJson(meta)
    }

    @TypeConverter
    fun stringToCovers(covers: String): List<Covers>? {
        return moshi.adapter<List<Covers>>(Covers::class.java).fromJson(covers)
    }

    @TypeConverter
    fun coversToString(covers: List<Covers>): String {
        return moshi.adapter<List<Covers>>(Covers::class.java).toJson(covers)
    }

    @TypeConverter
    fun stringToSeries(series: String): List<Series>? {
        return moshi.adapter<List<Series>>(Series::class.java).fromJson(series)
    }

    @TypeConverter
    fun seriesToString(series: List<Series>): String {
        return moshi.adapter<List<Series>>(Series::class.java).toJson(series)
    }

    @TypeConverter
    fun stringToMovies(movies: String): List<Movies>? {
        return moshi.adapter<List<Movies>>(Movies::class.java).fromJson(movies)
    }

    @TypeConverter
    fun moviesToString(movies: List<Movies>): String {
        return moshi.adapter<List<Movies>>(Movies::class.java).toJson(movies)
    }

    @TypeConverter
    fun stringToFeatured(featured: String): List<Featured>? {
        return moshi.adapter<List<Featured>>(Featured::class.java).fromJson(featured)
    }

    @TypeConverter
    fun featuredToString(featured: List<Featured>): String {
        return moshi.adapter<List<Featured>>(Featured::class.java).toJson(featured)
    }
}