package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.ui.BaseApplication
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.API_EXTRACTOR
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser


object Vidoza {

    fun getFasterLink(l: String?): String? {
        val authJSON: String = BaseApplication.AUTH
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "vidoza"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", UrlResolver().encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}
