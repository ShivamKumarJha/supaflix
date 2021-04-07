package com.shivamkumarjha.supaflix.repository

import androidx.paging.PagingSource
import com.shivamkumarjha.supaflix.model.xmovies.*
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.flow.Flow

interface XmoviesRepository {
    suspend fun home(): Flow<Resource<Home?>>
    suspend fun trending(): Flow<Resource<ContentsResponse?>>
    suspend fun featured(): Flow<Resource<ContentsResponse?>>
    suspend fun recentMovies(): Flow<Resource<ContentsResponse?>>
    suspend fun mostViewedMovies(): Flow<Resource<ContentsResponse?>>
    suspend fun topRatedMovies(): Flow<Resource<ContentsResponse?>>
    suspend fun topIMBDMovies(): Flow<Resource<ContentsResponse?>>
    suspend fun recentSeries(): Flow<Resource<ContentsResponse?>>
    suspend fun mostViewedSeries(): Flow<Resource<ContentsResponse?>>
    suspend fun topRatedSeries(): Flow<Resource<ContentsResponse?>>
    suspend fun topIMBDSeries(): Flow<Resource<ContentsResponse?>>
    suspend fun releaseList(): Flow<Resource<ReleaseList?>>
    suspend fun content(hash: String): Flow<Resource<Content?>>
    suspend fun embeds(contentHash: String, episodeHash: String): Flow<Resource<EmbedsResponse?>>
    suspend fun server(
        contentHash: String,
        episodeHash: String,
        serverHash: String
    ): Flow<Resource<ServerResponse?>>

    //Paging
    fun movies(): PagingSource<Int, Contents>
    fun series(): PagingSource<Int, Contents>
    fun genre(hash: String, slug: String): PagingSource<Int, Contents>
    fun filter(filterQuery: FilterQuery): PagingSource<Int, Contents>
    fun search(keyword: String): PagingSource<Int, Contents>
}