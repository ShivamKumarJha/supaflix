package com.shivamkumarjha.supaflix.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shivamkumarjha.supaflix.BuildConfig
import com.shivamkumarjha.supaflix.network.ConnectionLiveData
import com.shivamkumarjha.supaflix.network.HttpInterceptor
import com.shivamkumarjha.supaflix.network.NetworkHelper
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun getNetworkHelper(connectivityManager: ConnectivityManager) =
        NetworkHelper(connectivityManager)

    @Provides
    @Singleton
    fun getConnectionLiveData(connectivityManager: ConnectivityManager) =
        ConnectionLiveData(connectivityManager)

    @Provides
    @Singleton
    fun getHttpInterceptor(
        networkHelper: NetworkHelper
    ) = HttpInterceptor(networkHelper, NoConnectivityException())

    @Provides
    @Singleton
    fun getGson(): Gson = GsonBuilder().setLenient()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    @Singleton
    fun getCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun getOkHTTPClient(cache: Cache, httpInterceptor: HttpInterceptor): OkHttpClient {
        //Logging
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        //OkHttpClient
        val client = OkHttpClient.Builder()
        client.connectTimeout(5, TimeUnit.MINUTES)
        client.readTimeout(5, TimeUnit.MINUTES)
        client.addInterceptor(httpInterceptor)
        client.addInterceptor(logging)
        client.cache(cache)
        client.retryOnConnectionFailure(true)
        client.connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        return client.build()
    }
}