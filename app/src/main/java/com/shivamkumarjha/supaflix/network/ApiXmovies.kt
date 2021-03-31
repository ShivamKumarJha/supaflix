package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.ContentsPagingResponse
import com.shivamkumarjha.supaflix.model.xmovies.ContentsResponse
import com.shivamkumarjha.supaflix.model.xmovies.Home
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiXmovies {

    @GET("pages/home")
    suspend fun home(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<Home>

    @GET("contents/home-module/trending")
    suspend fun trending(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/featured")
    suspend fun featured(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/recent-movies")
    suspend fun recentMovies(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/most-viewed-movies")
    suspend fun mostViewedMovies(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/top-rated-movies")
    suspend fun topRatedMovies(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/top-imdb-movies")
    suspend fun topIMBDMovies(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/recent-series")
    suspend fun recentSeries(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/most-viewed-series")
    suspend fun mostViewedSeries(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/top-rated-series")
    suspend fun topRatedSeries(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("contents/home-module/top-imdb-series")
    suspend fun topIMBDSeries(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<ContentsResponse>

    @GET("pages/movies")
    suspend fun movies(
        @Query("site") site: String = Constants.XMOVIES8_SITE_CODE,
        @Query("page") page: Int
    ): Response<ContentsPagingResponse>

    @GET("pages/tvseries")
    suspend fun series(
        @Query("site") site: String = Constants.XMOVIES8_SITE_CODE,
        @Query("page") page: Int
    ): Response<ContentsPagingResponse>
}