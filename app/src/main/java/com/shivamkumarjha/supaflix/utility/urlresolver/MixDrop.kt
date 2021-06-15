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
import java.util.*
import java.util.regex.Pattern


object MixDrop {

    fun getFasterLink(l: String): String? {
        var link = l
        link = link.replace("/f/", "/e/")
        var document: Document?
        val headers = "Referer@$link"
        val mapHeaders: MutableMap<String, String> = HashMap()
        val hd = ArrayList(listOf(*headers.split("@".toRegex()).toTypedArray()))
        for (i in hd.indices) {
            if (i % 2 == 0) mapHeaders[hd[i]] = hd[i + 1]
        }
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .headers(mapHeaders)
                .parser(Parser.htmlParser()).get()
            if (document == null || !document.toString().contains("eval(")) {
                if (document != null) {
                    val p = Pattern.compile("window.location\\s*=\\s*\"(.*?)\"", Pattern.DOTALL)
                    val m = p.matcher(document.toString())
                    if (m.find()) {
                        val token = m.group(1)
                        if (token != null && token.isNotEmpty()) {
                            link = link.split("/e/".toRegex()).toTypedArray()[0] + token
                            document = Jsoup.connect(link)
                                .timeout(TIMEOUT_EXTRACT_MILS)
                                .headers(mapHeaders)
                                .parser(Parser.htmlParser()).get()
                        }
                    }
                }
            }
            try {
                val apiURL: String = API_EXTRACTOR + "mixdrop"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", UrlResolver().encodeMSG(document.toString()))
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
