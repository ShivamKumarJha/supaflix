package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiVidCloud {

    @Headers("X-Requested-With:XMLHttpRequest")
    @GET("ajax.php")
    suspend fun getLinkWithoutTrack(
        @Query("id") id: String,
        @Query("sub") sub: String
    ): Response<LinkFrame>

    @Headers("X-Requested-With:XMLHttpRequest")
    @GET("ajax.php")
    suspend fun getLink(
        @Query("id") id: String,
        @Query("sub") sub: String
    ): Response<VidCloud>
}