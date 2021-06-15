package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_SECONDS
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern


object VidCloud {

    fun getFasterLink(l: String): String? {
        var mp4: String? = null
        val v = "JZfekeK8w6ZlhLfH_ZyseSLX"
        val cb = "ilzxej5hmdxe"
        val siteKey = "6LdqXa0UAAAAABc77NIcku_LdXJio9kaJVpYkgQJ"
        val co = "aHR0cHM6Ly92aWRjbG91ZC5ydTo0NDM."
        val sa = "get_playerr"
        val url =
            "https://www.google.com/recaptcha/api2/anchor?ar=1&k=$siteKey&co=$co&hl=es&v=$v&size=invisible&cb=$cb"
        var headers: MutableMap<String?, String?> = HashMap()
        headers["Accept"] = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
        headers["Accept-Language"] = "ro-RO,ro;q=0.8,en-US;q=0.6,en-GB;q=0.4,en;q=0.2"
        headers["referer"] = "https://vidcloud.ru"
        try {
            var data = Jsoup.connect(url)
                .timeout(TIMEOUT_SECONDS * 1000)
                .headers(headers)
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute().body()
            var p = Pattern.compile("recaptcha-token\"\\s*value\\s*=\\s*\"(.*?)\"", Pattern.DOTALL)
            var m = p.matcher(data)
            if (m.find()) {
                var token = m.group(1)
                val url2 =
                    "https://www.google.com/recaptcha/api2/reload?k=$siteKey"
                val params: MutableMap<String, String> = HashMap()
                params["v"] = v
                params["reason"] = "q"
                params["k"] = siteKey
                params["c"] = token
                params["sa"] = sa
                params["co"] = co
                val post: String = UrlResolver().urlEncodeUTF8(params)
                headers = HashMap()
                headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8"
                headers["Referer"] = l
                data = Jsoup.connect("$url2&$post")
                    .timeout(TIMEOUT_SECONDS * 1000)
                    .headers(headers)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute().body()
                p = Pattern.compile("\\/[v|embed]\\/([a-zA-Z0-9_]+)", Pattern.DOTALL)
                m = p.matcher(l)
                if (m.find()) {
                    val streamid = m.group(1)
                    p = Pattern.compile("rresp\\s*\"\\s*,\\s*\"\\s*(.*?)\"", Pattern.DOTALL)
                    m = p.matcher(data)
                    if (m.find()) {
                        token = m.group(1)
                        val page = if (l.contains("/embed")) "embed" else "video"
                        headers = HashMap()
                        headers["Referer"] = l
                        headers["Connection"] = "keep-alive"
                        headers["Accept-Encoding"] = "gzip, deflate, br"
                        headers["Accept"] = "application/json, text/javascript, */*; q=0.01"
                        headers["Accept-Language"] =
                            "res-ES,es;q=0.9,en-US;q=0.8,en-GB;q=0.7,en;q=0.6,fr;q=0.5,id;q=0.4"
                        data = Jsoup.connect("https://vidcloud.ru/player")
                            .timeout(TIMEOUT_SECONDS * 1000)
                            .headers(headers)
                            .method(Connection.Method.GET)
                            .data("token", token)
                            .data("page", page)
                            .data("fid", streamid)
                            .ignoreContentType(true)
                            .execute().body()
                        if (data != null && data.isNotEmpty()) {
                            var dataJson: String? = JSONObject(data).getString("html")
                            val pattern = Pattern.compile("sources\\s*=\\s*\\[(.*?)]")
                            val matcher = pattern.matcher(dataJson)
                            dataJson = null
                            while (matcher.find()) {
                                dataJson = matcher.group(1)
                            }
                            if (dataJson != null) {
                                val arr = JSONArray("[$dataJson]")
                                for (i in 0 until arr.length()) {
                                    mp4 = arr.getJSONObject(i).getString("file")
                                    if (mp4.contains(".mp4")) break
                                }
                                if (mp4 != null)
                                    mp4 = mp4.replace("\\", "")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}
