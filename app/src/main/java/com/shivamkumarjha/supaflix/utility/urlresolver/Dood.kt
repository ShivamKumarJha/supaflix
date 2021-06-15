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
import java.util.regex.Pattern


object Dood {

    fun getFasterLink(l: String): String? {
        var link = l
        val authJSON: String = BaseApplication.AUTH
        var document: Document?
        var mp4: String? = null
        link = link.replace("/e/", "/d/")
        val embedRegex = "\\/pass_md5\\/(.*?)[\'|\"]"
        val downloadRegex = "\\/download\\/(.*?)[\'|\"]"
        try {
            val apiURL: String = API_EXTRACTOR + "dood"
            // try with download mode
            try {
                document = Jsoup.connect(link)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .userAgent("Mozilla")
                    .referrer(link)
                    .parser(Parser.htmlParser()).get()
                val p = Pattern.compile(downloadRegex, Pattern.DOTALL)
                val m = p.matcher(document.toString())
                if (m.find()) {
                    val downloadLink = "https://dood.watch/download/" + m.group(1)
                    document = Jsoup.connect(downloadLink)
                        .timeout(TIMEOUT_EXTRACT_MILS)
                        .userAgent("Mozilla")
                        .referrer(link)
                        .parser(Parser.htmlParser()).get()
                    try {
                        //
                        val obj = Jsoup.connect(apiURL)
                            .timeout(TIMEOUT_EXTRACT_MILS)
                            .data("mode", "direct")
                            .data("auth", UrlResolver().encodeMSG(authJSON))
                            .data("source", UrlResolver().encodeMSG(document.toString()))
                            .method(Connection.Method.POST)
                            .ignoreContentType(true)
                            .execute().body()
                        if (obj != null && obj.contains("url")) {
                            val json = JSONObject(obj)
                            if (json.getString("status") == "ok") mp4 = json.getString("url")
                        }
                    } catch (e: Exception) {
                        Log.e(Constants.TAG, e.message ?: "Error")
                    }
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, e.message ?: "Error")
            }
            if (mp4 == null || mp4.isEmpty()) {
                // try with embed mode
                link = link.replace("/d/", "/e/").replace("/h/", "/e/")
                try {
                    document = Jsoup.connect(link)
                        .timeout(TIMEOUT_EXTRACT_MILS)
                        .userAgent("Mozilla")
                        .referrer(link)
                        .parser(Parser.htmlParser()).get()
                    val p = Pattern.compile(embedRegex, Pattern.DOTALL)
                    val m = p.matcher(document.toString())
                    if (m.find()) {
                        val pasrs = m.group(1)
                        val embedLink = "https://dood.watch/pass_md5/$pasrs"
                        document = Jsoup.connect(embedLink)
                            .timeout(TIMEOUT_EXTRACT_MILS)
                            .userAgent("Mozilla")
                            .referrer(link)
                            .parser(Parser.htmlParser()).get()
                        try {
                            //
                            val obj = Jsoup.connect(apiURL)
                                .timeout(TIMEOUT_EXTRACT_MILS)
                                .data("mode", "embed")
                                .data("token", pasrs)
                                .data("auth", UrlResolver().encodeMSG(authJSON))
                                .data("source", UrlResolver().encodeMSG(document.toString()))
                                .method(Connection.Method.POST)
                                .ignoreContentType(true)
                                .execute().body()
                            if (obj != null && obj.contains("url")) {
                                val json = JSONObject(obj)
                                if (json.getString("status") == "ok") mp4 = json.getString("url")
                            }
                        } catch (e: Exception) {
                            Log.e(Constants.TAG, e.message ?: "Error")
                        }
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
