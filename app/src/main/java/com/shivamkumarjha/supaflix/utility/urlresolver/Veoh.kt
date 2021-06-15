package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.API_EXTRACTOR
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


object Veoh {

    fun getFasterLink(l: String): String? {
        val authJSON: String = UrlResolver().getCheckString()
        val document: String?
        var mp4: String? = null
        try {
            val video =
                if (l.contains("/getVideo/")) l else "https://www.veoh.com/watch/getVideo/" + l.split(
                    "/".toRegex()
                ).toTypedArray()[4]
            document = Jsoup.connect(video)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute().body()
            try {
                val apiURL: String = API_EXTRACTOR + "veoh"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", UrlResolver().encodeMSG(authJSON))
                    .data("source", UrlResolver().encodeMSG(document))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}
