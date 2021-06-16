package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.model.urlresolver.UrlResolverResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiUrlResolver {

    @POST("dood")
    suspend fun dood(
        @Query("mode") mode: String,
        @Query("source") source: String
    ): Response<UrlResolverResponse>
}