package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.urlresolver.UrlResolverResponse
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface UrlResolverRepository {
    suspend fun dood(mode: String, source: String): Flow<Resource<UrlResolverResponse?>>
}