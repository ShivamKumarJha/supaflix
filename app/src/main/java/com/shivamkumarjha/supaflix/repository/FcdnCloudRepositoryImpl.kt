package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import com.shivamkumarjha.supaflix.network.ApiFcdnCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FcdnCloudRepositoryImpl(private val apiFcdnCloud: ApiFcdnCloud) : FcdnCloudRepository {
    override suspend fun getLink(hash: String): Flow<Fcdn?> = flow {
        try {
            val response = apiFcdnCloud.getLink(hash)
            if (response.isSuccessful) {
                emit(response.body())
                Log.d(Constants.TAG, response.body().toString())
            }
        } catch (exception: Exception) {
            Log.d(Constants.TAG, exception.message.toString())
        }
    }.flowOn(Dispatchers.IO)
}