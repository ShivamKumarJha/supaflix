package com.shivamkumarjha.supaflix.repository

import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface VidCloudRepository {
    suspend fun getLinkWithoutTrack(id: String, sub: String): Flow<Resource<LinkFrame?>>
    suspend fun getLink(id: String, sub: String): Flow<Resource<VidCloud?>>
}