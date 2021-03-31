package com.shivamkumarjha.supaflix.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    fun getClient(baseUrl: String, okHttpClient: OkHttpClient, mosh: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(mosh))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }
}