package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiGocdnCloud {
    @Headers("X-Requested-With:XMLHttpRequest")
    @GET("get_stream")
    suspend fun getLink(@Query("video_hash") video_hash: String): Response<Gocdn>
}