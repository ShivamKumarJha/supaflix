package com.shivamkumarjha.supaflix.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHelper(private val connectivityManager: ConnectivityManager) {

    fun isNetworkConnected(): Boolean {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}