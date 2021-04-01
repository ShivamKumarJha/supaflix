package com.shivamkumarjha.supaflix.di

import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.network.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getApiFcdnCloud(okHttpClient: OkHttpClient, mosh: Moshi): ApiFcdnCloud =
        RetrofitClient.getClient(Constants.FCDN_CLOUD_URL, okHttpClient, mosh)
            .create(ApiFcdnCloud::class.java)

    @Provides
    @Singleton
    fun getApiGocdnCloud(okHttpClient: OkHttpClient, mosh: Moshi): ApiGocdnCloud =
        RetrofitClient.getClient(Constants.GOCDN_CLOUD_URL, okHttpClient, mosh)
            .create(ApiGocdnCloud::class.java)

    @Provides
    @Singleton
    fun getApiMovCloud(okHttpClient: OkHttpClient, mosh: Moshi): ApiMovCloud =
        RetrofitClient.getClient(Constants.MOV_CLOUD_URL, okHttpClient, mosh)
            .create(ApiMovCloud::class.java)

    @Provides
    @Singleton
    fun getApiVidCloud(okHttpClient: OkHttpClient, mosh: Moshi): ApiVidCloud =
        RetrofitClient.getClient(Constants.VID_CLOUD_URL, okHttpClient, mosh)
            .create(ApiVidCloud::class.java)

    @Provides
    @Singleton
    fun getApiXmovies(okHttpClient: OkHttpClient, mosh: Moshi): ApiXmovies =
        RetrofitClient.getClient(Constants.XMOVIES8_URL, okHttpClient, mosh)
            .create(ApiXmovies::class.java)
}