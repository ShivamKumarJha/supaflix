package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.model.movcloud.MovCloud
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiMovCloud {

    @GET("stream/{hash}")
    suspend fun getLink(@Path("hash") hash: String): Response<MovCloud>
}