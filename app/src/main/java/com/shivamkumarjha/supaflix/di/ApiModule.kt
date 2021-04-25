package com.shivamkumarjha.supaflix.di

import com.google.gson.Gson
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.network.*
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
    fun getApiFcdnCloud(okHttpClient: OkHttpClient, gson: Gson): ApiFcdnCloud =
        RetrofitClient.getClient(Constants.FCDN_CLOUD_URL, okHttpClient, gson)
            .create(ApiFcdnCloud::class.java)

    @Provides
    @Singleton
    fun getApiGocdnCloud(okHttpClient: OkHttpClient, gson: Gson): ApiGocdnCloud =
        RetrofitClient.getClient(Constants.GOCDN_CLOUD_URL, okHttpClient, gson)
            .create(ApiGocdnCloud::class.java)

    @Provides
    @Singleton
    fun getApiMovCloud(okHttpClient: OkHttpClient, gson: Gson): ApiMovCloud =
        RetrofitClient.getClient(Constants.MOV_CLOUD_URL, okHttpClient, gson)
            .create(ApiMovCloud::class.java)

    @Provides
    @Singleton
    fun getApiVidCloud(okHttpClient: OkHttpClient, gson: Gson): ApiVidCloud =
        RetrofitClient.getClient(Constants.VID_CLOUD_URL, okHttpClient, gson)
            .create(ApiVidCloud::class.java)

    @Provides
    @Singleton
    fun getApiXmovies(okHttpClient: OkHttpClient, gson: Gson): ApiXmovies =
        RetrofitClient.getClient(Constants.XMOVIES8_URL, okHttpClient, gson)
            .create(ApiXmovies::class.java)
}