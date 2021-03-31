package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import kotlinx.coroutines.flow.Flow

interface VidCloudRepository {
    suspend fun getLinkWithoutTrack(id: String, sub: String): Flow<LinkFrame?>
    suspend fun getLink(id: String, sub: String): Flow<VidCloud?>
}