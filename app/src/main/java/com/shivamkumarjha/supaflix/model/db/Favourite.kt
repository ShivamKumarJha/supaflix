package com.shivamkumarjha.supaflix.model.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favourites")
data class Favourite(
    @PrimaryKey val hash: String,
    val title: String,
    val poster: String,
    val released: String?,
    val imbd: Double?,
    val description: String?
) : Parcelable