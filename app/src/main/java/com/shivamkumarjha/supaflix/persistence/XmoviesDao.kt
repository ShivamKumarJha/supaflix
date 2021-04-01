package com.shivamkumarjha.supaflix.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamkumarjha.supaflix.model.xmovies.Home

@Dao
interface XmoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHome(home: Home)

    @Query("SELECT * FROM home WHERE homeId=:homeId")
    fun getHome(homeId: Int = 0): Home?
}