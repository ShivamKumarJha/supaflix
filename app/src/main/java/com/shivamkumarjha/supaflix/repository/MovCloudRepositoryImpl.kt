package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.movcloud.MovCloud
import com.shivamkumarjha.supaflix.network.ApiMovCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovCloudRepositoryImpl(private val apiMovCloud: ApiMovCloud) : MovCloudRepository {
    override suspend fun getLink(hash: String): Flow<MovCloud?> = flow {
        try {
            val response = apiMovCloud.getLink(hash)
            if (response.isSuccessful) {
                emit(response.body())
                Log.d(Constants.TAG, response.body().toString())
            }
        } catch (exception: Exception) {
            Log.d(Constants.TAG, exception.message.toString())
        }
    }.flowOn(Dispatchers.IO)
}