package com.shivamkumarjha.supaflix.repository

import android.util.Log
import androidx.paging.PagingSource
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.*
import com.shivamkumarjha.supaflix.network.ApiXmovies
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import com.shivamkumarjha.supaflix.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class XmoviesRepositoryImpl(private val apiXmovies: ApiXmovies) : XmoviesRepository {
    override suspend fun home(): Flow<Resource<Home?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.home()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun trending(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.trending()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun featured(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.featured()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun recentMovies(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.recentMovies()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun mostViewedMovies(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.mostViewedMovies()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun topRatedMovies(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.topRatedMovies()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun topIMBDMovies(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.topIMBDMovies()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun recentSeries(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.recentSeries()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun mostViewedSeries(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.mostViewedSeries()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun topRatedSeries(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.topRatedSeries()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun topIMBDSeries(): Flow<Resource<ContentsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.topIMBDSeries()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun releaseList(): Flow<Resource<ReleaseList?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.releaseList()
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun watch(hash: String): Flow<Resource<Watch?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.watch(hash = hash)
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun embeds(
        contentHash: String,
        episodeHash: String
    ): Flow<Resource<EmbedsResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.embeds(contentHash, episodeHash)
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun server(
        contentHash: String,
        episodeHash: String,
        serverHash: String
    ): Flow<Resource<ServerResponse?>> = flow {
        emit(Resource.loading(data = null))
        try {
            val response = apiXmovies.server(contentHash, episodeHash, serverHash)
            if (response.isSuccessful) {
                emit(Resource.success(data = response.body()))
                Log.d(Constants.TAG, response.body().toString())
            } else {
                emit(Resource.error(data = null, message = response.code().toString()))
                Log.d(Constants.TAG, response.code().toString())
            }
        } catch (exception: Exception) {
            if (exception is NoConnectivityException)
                emit(Resource.offline(data = null))
            else {
                emit(Resource.error(data = null, message = exception.message.toString()))
                Log.e(Constants.TAG, exception.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun movies(): PagingSource<Int, ContentsPagingResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun series(): PagingSource<Int, ContentsPagingResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun genre(hash: String, slug: String): PagingSource<Int, GenreResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun filter(filterQuery: FilterQuery): PagingSource<Int, FilterResponse> {
        TODO("Not yet implemented")
    }
}