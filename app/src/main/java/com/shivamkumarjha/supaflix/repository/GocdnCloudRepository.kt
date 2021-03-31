package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import kotlinx.coroutines.flow.Flow

interface GocdnCloudRepository {
    suspend fun getLink(video_hash: String): Flow<Gocdn?>
}