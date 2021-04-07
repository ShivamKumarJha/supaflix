package com.shivamkumarjha.supaflix.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamkumarjha.supaflix.model.db.DbContents
import com.shivamkumarjha.supaflix.model.db.DbHome
import com.shivamkumarjha.supaflix.model.xmovies.Content

@Dao
interface XmoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHome(dbHome: DbHome)

    @Query("DELETE FROM home")
    fun clearHome()

    @Query("SELECT * FROM home where id=:id")
    fun getHome(id: Int = 0): DbHome?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContents(dbContents: DbContents)

    @Query("DELETE FROM contents where id=:id")
    fun clearContents(id: Int = 0)

    @Query("SELECT * FROM contents where id=:id")
    fun getContents(id: Int = 0): DbContents?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContent(content: Content)

    @Query("SELECT * FROM content where hash=:hash")
    fun getContent(hash: String): Content?
}