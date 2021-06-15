package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.API_EXTRACTOR
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser


object SuperVideo {

    fun getFasterLink(l: String?): String? {
        if (l == null)
            return null
        val authJSON: String = UrlResolver().getCheckString()
        val document: Document?
        var mp4: String? = null
        val apiURL: String = API_EXTRACTOR + "supervideo"
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", UrlResolver().encodeMSG(document.toString()))
                    .data("auth", UrlResolver().encodeMSG(authJSON))
                    .data("mode", "local")
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
            if (mp4 == null || mp4.isEmpty()) {
                try {
                    val obj = Jsoup.connect(apiURL)
                        .timeout(TIMEOUT_EXTRACT_MILS)
                        .data("source", UrlResolver().encodeMSG(l))
                        .data("auth", UrlResolver().encodeMSG(authJSON))
                        .data("mode", "remote")
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
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}