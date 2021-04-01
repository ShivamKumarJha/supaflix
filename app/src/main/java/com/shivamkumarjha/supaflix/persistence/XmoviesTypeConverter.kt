package com.shivamkumarjha.supaflix.persistence

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.shivamkumarjha.supaflix.model.xmovies.Home
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class XmoviesTypeConverter(private val moshi: Moshi) {
    @TypeConverter
    fun stringToMHome(home: String): Home? {
        return moshi.adapter(Home::class.java).fromJson(home)
    }

    @TypeConverter
    fun homeToString(home: Home): String {
        return moshi.adapter(Home::class.java).toJson(home)
    }
}