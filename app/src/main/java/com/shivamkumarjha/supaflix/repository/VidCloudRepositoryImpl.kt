package com.shivamkumarjha.supaflix.repository

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import com.shivamkumarjha.supaflix.network.ApiVidCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class VidCloudRepositoryImpl(private val vidCloudApi: ApiVidCloud) : VidCloudRepository {

    override suspend fun getLinkWithoutTrack(id: String, sub: String): Flow<LinkFrame?> = flow {
        try {
            val response = vidCloudApi.getLinkWithoutTrack(id, sub)
            if (response.isSuccessful) {
                emit(response.body())
                Log.d(Constants.TAG, response.body().toString())
            }
        } catch (exception: Exception) {
            Log.d(Constants.TAG, exception.message.toString())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getLink(id: String, sub: String): Flow<VidCloud?> = flow {
        try {
            val response = vidCloudApi.getLink(id, sub)
            if (response.isSuccessful) {
                emit(response.body())
                Log.d(Constants.TAG, response.body().toString())
            }
        } catch (exception: Exception) {
            if (exception.message.toString().contains("java.lang.IllegalStateException")) {
                val dummyVidCloud = VidCloud(null, null, Constants.VID_CLOUD_NO_TRACK)
                emit(dummyVidCloud)
            }
            Log.d(Constants.TAG, exception.message.toString())
        }
    }.flowOn(Dispatchers.IO)
}