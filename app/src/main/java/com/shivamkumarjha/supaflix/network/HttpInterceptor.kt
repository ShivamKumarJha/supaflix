package com.shivamkumarjha.supaflix.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor(
    private val networkHelper: NetworkHelper,
    private val noConnectivityException: NoConnectivityException
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkHelper.isNetworkConnected())
            throw noConnectivityException

        return chain.proceed(chain.request())
    }
}