package com.shivamkumarjha.supaflix.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamkumarjha.supaflix.model.db.DbHome

@Dao
interface XmoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHome(dbHome: DbHome)

    @Query("DELETE FROM home")
    fun clearHome()

    @Query("SELECT * FROM home where id=:id")
    fun getHome(id: Int = 0): DbHome?
}