package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import com.shivamkumarjha.supaflix.network.ApiFcdnCloud
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FcdnCloudRepositoryImpl(private val apiFcdnCloud: ApiFcdnCloud) : FcdnCloudRepository {
    override suspend fun getLink(hash: String): Flow<Resource<Fcdn?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiFcdnCloud.getLink(hash)
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)
}