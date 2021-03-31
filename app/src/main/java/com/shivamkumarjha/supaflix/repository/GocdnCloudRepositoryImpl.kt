package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import com.shivamkumarjha.supaflix.network.ApiGocdnCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GocdnCloudRepositoryImpl(private val apiGocdnCloud: ApiGocdnCloud) : GocdnCloudRepository {
    override suspend fun getLink(video_hash: String): Flow<Gocdn?> = flow {
        try {
            val response = apiGocdnCloud.getLink(video_hash)
            if (response.isSuccessful) {
                emit(response.body())
                Log.d(Constants.TAG, response.body().toString())
            }
        } catch (exception: Exception) {
            Log.d(Constants.TAG, exception.message.toString())
        }
    }.flowOn(Dispatchers.IO)
}