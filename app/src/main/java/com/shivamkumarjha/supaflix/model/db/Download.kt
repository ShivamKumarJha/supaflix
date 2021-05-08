package com.shivamkumarjha.supaflix.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download")
data class Download(
    @PrimaryKey val downloadID: Long,
    val url: String,
    val type: String,
    val history: History,
    var filePath: String? = null
)