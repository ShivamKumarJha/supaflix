package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.movcloud.MovCloud
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovCloudRepository {
    suspend fun getLink(hash: String): Flow<Resource<MovCloud?>>
}