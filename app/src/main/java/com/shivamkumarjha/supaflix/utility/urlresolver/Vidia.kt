package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.ui.BaseApplication
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.API_EXTRACTOR
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


object Vidia {

    fun getFasterLink(l: String?): String? {
        if (l == null)
            return null

        val authJSON: String = BaseApplication.AUTH
        var mp4: String? = null
        try {
            val apiURL: String = API_EXTRACTOR + "vidia"
            val obj = Jsoup.connect(apiURL)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .data("source", UrlResolver().encodeMSG(l))
                .data("auth", UrlResolver().encodeMSG(authJSON))
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body()
            if (obj != null && obj.contains("url")) {
                val json = JSONObject(obj)
                if (json.getString("status") == "ok")
                    mp4 = json.getString("url")
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}