package com.shivamkumarjha.supaflix.network

import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import retrofit2.Response
import retrofit2.http.*

interface ApiFcdnCloud {

    @Headers("X-Requested-With:XMLHttpRequest")
    @FormUrlEncoded
    @POST("source/{hash}")
    suspend fun getLink(
        @Path("hash") hash: String,
        @Field("r") r: String = Constants.FCDN_CLOUD_R,
        @Field("d") d: String = Constants.FCDN_CLOUD_D
    ): Response<Fcdn>
}