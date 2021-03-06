package com.shivamkumarjha.supaflix.model.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history")
data class History(
    @PrimaryKey val historyId: Int,
    val hash: String,
    val title: String,
    val poster: String,
    val released: String?,
    val imbd: String?,
    val description: String?,
    val episode: String,
    val episodeHash: String,
    var window: Int = 0,
    var position: Long = 0L,
    var duration: Long = 0L
) : Parcelable