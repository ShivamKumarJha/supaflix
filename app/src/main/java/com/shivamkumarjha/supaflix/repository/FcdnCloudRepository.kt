package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface FcdnCloudRepository {
    suspend fun getLink(hash: String): Flow<Resource<Fcdn?>>
}