package com.shivamkumarjha.supaflix.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.DbContents
import com.shivamkumarjha.supaflix.model.db.DbHome
import com.shivamkumarjha.supaflix.model.xmovies.*
import com.shivamkumarjha.supaflix.network.ApiXmovies
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.persistence.XmoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class XmoviesRepositoryImpl(
    private val apiXmovies: ApiXmovies,
    private val xmoviesDao: XmoviesDao
) : XmoviesRepository {
    override suspend fun home(): Flow<Resource<Home?>> = flow {
        emit(Resource.loading(data = null))
        try {
            //Get from database
            val dbData = xmoviesDao.getHome()
            if (dbData != null) {
                emit(Resource.loading(data = dbData.home))
            }
            //API call
            val response = apiXmovies.home()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearHome()
                    xmoviesDao.addHome(DbHome(home = responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(0)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.trending()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(0)
                    xmoviesDao.addContents(DbContents(0, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(1)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.featured()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(1)
                    xmoviesDao.addContents(DbContents(1, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(2)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.recentMovies()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(2)
                    xmoviesDao.addContents(DbContents(2, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(3)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.mostViewedMovies()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(3)
                    xmoviesDao.addContents(DbContents(3, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(4)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.topRatedMovies()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(4)
                    xmoviesDao.addContents(DbContents(4, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(5)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.topIMBDMovies()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(5)
                    xmoviesDao.addContents(DbContents(5, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(6)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.recentSeries()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(6)
                    xmoviesDao.addContents(DbContents(6, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(7)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.mostViewedSeries()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(7)
                    xmoviesDao.addContents(DbContents(7, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(8)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.topRatedSeries()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(8)
                    xmoviesDao.addContents(DbContents(8, responseData))
                }
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
            //Get from database
            val dbData = xmoviesDao.getContents(9)
            if (dbData != null) {
                emit(Resource.loading(data = dbData.contentsResponse))
            }
            //API call
            val response = apiXmovies.topIMBDSeries()
            if (response.isSuccessful) {
                val responseData = response.body()
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.clearContents(9)
                    xmoviesDao.addContents(DbContents(9, responseData))
                }
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

    override suspend fun content(hash: String): Flow<Resource<Content?>> = flow {
        emit(Resource.loading(data = null))
        try {
            //Get from database
            val dbData = xmoviesDao.getContent(hash)
            if (dbData != null) {
                emit(Resource.loading(data = dbData))
            }
            //API call
            val response = apiXmovies.watch(hash = hash)
            if (response.isSuccessful) {
                val responseData = response.body()?.content
                emit(Resource.success(data = responseData))
                Log.d(Constants.TAG, responseData.toString())
                //Save to database
                if (responseData != null) {
                    xmoviesDao.addContent(responseData)
                }
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

    override fun movies(): PagingSource<Int, Contents> {
        return object : PagingSource<Int, Contents>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contents> {
                return try {
                    val currentPage = params.key ?: 1
                    val response = apiXmovies.movies(page = currentPage)
                    Log.d(Constants.TAG, response.body().toString())
                    val contents: ArrayList<Contents> = arrayListOf()
                    val data = response.body()?.contentList ?: emptyList()
                    contents.addAll(data)
                    LoadResult.Page(
                        data = contents,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (contents.isNullOrEmpty()) null else currentPage.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Contents>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
    }

    override fun series(): PagingSource<Int, Contents> {
        return object : PagingSource<Int, Contents>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contents> {
                return try {
                    val currentPage = params.key ?: 1
                    val response = apiXmovies.series(page = currentPage)
                    Log.d(Constants.TAG, response.body().toString())
                    val contents: ArrayList<Contents> = arrayListOf()
                    val data = response.body()?.contentList ?: emptyList()
                    contents.addAll(data)
                    LoadResult.Page(
                        data = contents,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (contents.isNullOrEmpty()) null else currentPage.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Contents>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
    }

    override fun genre(hash: String, slug: String): PagingSource<Int, Contents> {
        return object : PagingSource<Int, Contents>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contents> {
                return try {
                    val currentPage = params.key ?: 1
                    val response = apiXmovies.genre(page = currentPage, hash = hash, slug = slug)
                    Log.d(Constants.TAG, response.body().toString())
                    val contents: ArrayList<Contents> = arrayListOf()
                    val data = response.body()?.contentList ?: emptyList()
                    contents.addAll(data)
                    LoadResult.Page(
                        data = contents,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (contents.isNullOrEmpty()) null else currentPage.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Contents>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
    }

    override fun filter(filterQuery: FilterQuery): PagingSource<Int, Contents> {
        return object : PagingSource<Int, Contents>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contents> {
                return try {
                    val currentPage = params.key ?: 1
                    val response = apiXmovies.filter(page = currentPage, filterQuery = filterQuery)
                    Log.d(Constants.TAG, response.body().toString())
                    val contents: ArrayList<Contents> = arrayListOf()
                    val data = response.body()?.contentList ?: emptyList()
                    contents.addAll(data)
                    LoadResult.Page(
                        data = contents,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (contents.isNullOrEmpty()) null else currentPage.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Contents>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
    }

    override fun search(keyword: String): PagingSource<Int, Contents> {
        return object : PagingSource<Int, Contents>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contents> {
                return try {
                    val currentPage = params.key ?: 1
                    val response = apiXmovies.search(keyword, currentPage)
                    Log.d(Constants.TAG, response.body().toString())
                    val contents: ArrayList<Contents> = arrayListOf()
                    val data = response.body()?.results ?: emptyList()
                    contents.addAll(data)
                    LoadResult.Page(
                        data = contents,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (contents.isNullOrEmpty()) null else currentPage.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Contents>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
    }
}