package com.shivamkumarjha.supaflix.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shivamkumarjha.supaflix.model.db.*
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavourites(favourite: Favourite)

    @Transaction
    @Query("select * from favourites")
    fun getFavourites(): LiveData<List<Favourite>>

    @Query("DELETE FROM favourites")
    fun clearFavourites()

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE hash = :hash)")
    fun isFavourite(hash: String): Boolean

    @Delete
    fun removeFromFavourites(favourite: Favourite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToHistory(history: History)

    @Transaction
    @Query("select * from history ORDER BY historyId DESC")
    fun getHistory(): LiveData<List<History>>

    @Query("DELETE FROM history")
    fun clearHistory()

    @Delete
    fun removeFromHistory(history: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToDownload(download: Download)

    @Transaction
    @Query("select * from download ORDER BY downloadId DESC")
    fun getDownload(): LiveData<List<Download>>

    @Query("DELETE FROM download")
    fun clearDownload()

    @Delete
    fun removeFromDownload(download: Download)
}