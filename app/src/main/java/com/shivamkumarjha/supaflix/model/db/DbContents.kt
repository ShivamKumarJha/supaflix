package com.shivamkumarjha.supaflix.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shivamkumarjha.supaflix.model.xmovies.ContentsResponse

@Entity(tableName = "contents")
data class DbContents(
    @PrimaryKey val id: Int = 0,
    val contentsResponse: ContentsResponse
)