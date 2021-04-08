package com.shivamkumarjha.supaflix.repository

import androidx.lifecycle.LiveData
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.persistence.XmoviesDao

class DatabaseRepositoryImpl(private val xmoviesDao: XmoviesDao) : DatabaseRepository {
    override suspend fun addToFavourites(favourite: Favourite) {
        xmoviesDao.addToFavourites(favourite)
    }

    override suspend fun getFavourites(): LiveData<List<Favourite>> = xmoviesDao.getFavourites()

    override suspend fun clearFavourites() {
        xmoviesDao.clearFavourites()
    }

    override suspend fun isFavourite(hash: String): Boolean = xmoviesDao.isFavourite(hash)

    override suspend fun removeFromFavourites(favourite: Favourite) {
        xmoviesDao.removeFromFavourites(favourite)
    }

    override suspend fun addToHistory(history: History) {
        xmoviesDao.addToHistory(history)
    }

    override suspend fun getHistory(): LiveData<List<History>> = xmoviesDao.getHistory()

    override suspend fun clearHistory() {
        xmoviesDao.clearHistory()
    }

    override suspend fun removeFromHistory(history: History) {
        xmoviesDao.removeFromHistory(history)
    }
}