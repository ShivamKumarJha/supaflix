package com.shivamkumarjha.supaflix.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import com.shivamkumarjha.supaflix.config.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object Utility {
    private const val dateFormat = "yyyy-MM-dd"

    fun openLinkInBrowser(url: String, context: Context) {
        if (URLUtil.isValidUrl(url)) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openURL.data = Uri.parse(url)
            context.startActivity(openURL)
        }
    }

    suspend fun isURLReachable(link: String): Boolean {
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

    fun currentDate(): String {
        return try {
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            URLEncoder.encode(sdf.format(getEndOfDay(Date())), "utf-8")
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    private fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.time
    }
}