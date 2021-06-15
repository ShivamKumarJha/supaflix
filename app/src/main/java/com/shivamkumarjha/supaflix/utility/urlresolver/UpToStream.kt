package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.API_EXTRACTOR
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup

object UpToStream {

    fun getFasterLink(l: String): String? {
        var link = l
        var mp4: String? = null
        link = link.replace("uptobox.com", "uptostream.com")
        val file = link.split("/".toRegex()).toTypedArray()[3]
        var apiURL = "https://uptostream.com/api/streaming/source/get?token=null&file_code=$file"
        try {
            val document = Jsoup.connect(apiURL)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .referrer(link)
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute().body()
            try {
                apiURL = API_EXTRACTOR + "uptostream"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
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