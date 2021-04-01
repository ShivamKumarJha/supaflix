package com.shivamkumarjha.supaflix.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shivamkumarjha.supaflix.model.xmovies.Home

@Entity(tableName = "home")
data class DbHome(
    @PrimaryKey val id: Int = 0,
    val home: Home
)