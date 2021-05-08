package com.shivamkumarjha.supaflix.repository

import androidx.lifecycle.LiveData
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History

interface DatabaseRepository {
    suspend fun addToFavourites(favourite: Favourite)
    suspend fun getFavourites(): LiveData<List<Favourite>>
    suspend fun clearFavourites()
    suspend fun isFavourite(hash: String): Boolean
    suspend fun removeFromFavourites(favourite: Favourite)
    suspend fun addToHistory(history: History)
    suspend fun getHistory(): LiveData<List<History>>
    suspend fun clearHistory()
    suspend fun removeFromHistory(history: History)
    suspend fun addToDownload(download: Download)
    suspend fun getDownload(): LiveData<List<Download>>
    suspend fun clearDownload()
    suspend fun removeFromDownload(download: Download)
}