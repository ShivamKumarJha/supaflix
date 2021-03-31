package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface GocdnCloudRepository {
    suspend fun getLink(video_hash: String): Flow<Resource<Gocdn?>>
}