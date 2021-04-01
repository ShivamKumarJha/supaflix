package com.shivamkumarjha.supaflix.di

import android.content.Context
import android.net.ConnectivityManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.shivamkumarjha.supaflix.BuildConfig
import com.shivamkumarjha.supaflix.network.ConnectionLiveData
import com.shivamkumarjha.supaflix.network.HttpInterceptor
import com.shivamkumarjha.supaflix.network.NetworkHelper
import com.shivamkumarjha.supaflix.network.NoConnectivityException
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
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
    fun getConnectionLiveData(
        connectivityManager: ConnectivityManager,
        networkHelper: NetworkHelper
    ) = ConnectionLiveData(connectivityManager, networkHelper)

    @Provides
    @Singleton
    fun getHttpInterceptor(
        networkHelper: NetworkHelper
    ) = HttpInterceptor(networkHelper, NoConnectivityException())

    @Provides
    @Reusable
    fun providesMoshi(): Moshi = Moshi.Builder().build()

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
        client.addNetworkInterceptor(StethoInterceptor())
        client.cache(cache)
        client.retryOnConnectionFailure(true)
        client.connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        return client.build()
    }
}