package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Home
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiXmovies {

    @GET("pages/home")
    suspend fun home(@Query("site") site: String = Constants.XMOVIES8_SITE_CODE): Response<Home>
}