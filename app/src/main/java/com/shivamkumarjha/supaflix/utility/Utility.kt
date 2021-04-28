package com.shivamkumarjha.supaflix.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object Utility {
    fun openLinkInBrowser(url: String, context: Context) {
        if (URLUtil.isValidUrl(url)) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openURL.data = Uri.parse(url)
            context.startActivity(openURL)
        }
    }

    suspend fun isURLReachable(link: String?): Boolean {
        return GlobalScope.async(Dispatchers.IO) {
            try {
                val url = URL(link)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout = 5 * 1000
                httpURLConnection.connect()
                httpURLConnection.responseCode == 200
            } catch (exception: Exception) {
                Log.d(Constants.TAG, exception.message.toString())
                false
            }
        }.await()
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getWebLink(content: Content): String {
        return "https://xmovies8.pw/watch/${content.hash}/${content.slug}/watch-free.html"
    }
}