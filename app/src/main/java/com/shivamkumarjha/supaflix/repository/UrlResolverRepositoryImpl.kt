package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.urlresolver.UrlResolverResponse
import com.shivamkumarjha.supaflix.network.ApiUrlResolver
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UrlResolverRepositoryImpl(private val apiUrlResolver: ApiUrlResolver) :
    UrlResolverRepository {

    override suspend fun dood(mode: String, source: String): Flow<Resource<UrlResolverResponse?>> =
        flow {
            emit(Resource.loading(data = null))
            try {
                val response = apiUrlResolver.dood(mode, source)
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