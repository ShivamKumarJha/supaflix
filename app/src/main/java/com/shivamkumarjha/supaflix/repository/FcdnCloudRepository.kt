package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import kotlinx.coroutines.flow.Flow

interface FcdnCloudRepository {
    suspend fun getLink(hash: String): Flow<Fcdn?>
}