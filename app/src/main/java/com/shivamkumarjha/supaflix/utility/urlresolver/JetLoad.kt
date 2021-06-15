package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_SECONDS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern


object JetLoad {

    fun getFasterLink(l: String): String? {
        var link = l
        link = link.replace("/p/", "/e/")
        var mp4: String? = null
        val v = ""
        val cb = "123456789"
        val siteKey = "6Lc90MkUAAAAAOrqIJqt4iXY_fkXb7j3zwgRGtUI"
        val co = "aHR0cHM6Ly9qZXRsb2FkLm5ldDo0NDM."
        val sa = "secure_url"
        val url =
            "https://www.google.com/recaptcha/api2/anchor?ar=1&k=$siteKey&co=$co&hl=ro&v=$v&size=invisible&cb=$cb"
        var headers: MutableMap<String?, String?> = HashMap()
        headers["Accept"] = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
        headers["Accept-Language"] = "ro-RO,ro;q=0.8,en-US;q=0.6,en-GB;q=0.4,en;q=0.2"
        headers["referer"] = "https://jetload.net"
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
                var params: MutableMap<String?, String?> = HashMap()
                params["v"] = v
                params["reason"] = "q"
                params["k"] = siteKey
                params["c"] = token
                params["sa"] = sa
                params["co"] = co
                val post: String? = UrlResolver().urlEncodeUTF8(params)
                headers = HashMap()
                headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8"
                headers["Referer"] = link
                data = Jsoup.connect("$url2&$post")
                    .timeout(TIMEOUT_SECONDS * 1000)
                    .headers(headers)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute().body()
                p = Pattern.compile("\\/[p|e]\\/([a-zA-Z0-9_]+)", Pattern.DOTALL)
                m = p.matcher(link)
                if (m.find()) {
                    val streamId = m.group(1)
                    p = Pattern.compile("rresp\\s*\"\\s*,\\s*\"\\s*(.*?)\"", Pattern.DOTALL)
                    m = p.matcher(data)
                    if (m.find()) {
                        token = m.group(1)
                        params = HashMap()
                        params["token"] = token
                        params["stream_code"] = streamId
                        val pars =
                            "{\"token\":\"$token\",\"stream_code\":\"$streamId\"}"
                        headers = HashMap()
                        headers["Referer"] = link
                        headers["Connection"] = "keep-alive"
                        headers["Content-Length"] = pars.length.toString()
                        headers["Content-Type"] = "application/json;charset=UTF-8"
                        headers["Accept-Encoding"] = "gzip, deflate, br"
                        headers["Accept"] = "application/json, text/plain, */*"
                        headers["Accept-Language"] =
                            "res-ES,es;q=0.9,en-US;q=0.8,en-GB;q=0.7,en;q=0.6,fr;q=0.5,id;q=0.4"
                        data = Jsoup.connect("https://jetload.net/jet_secure")
                            .timeout(TIMEOUT_SECONDS * 1000)
                            .headers(headers)
                            .method(Connection.Method.POST)
                            .requestBody(pars)
                            .ignoreContentType(true)
                            .execute().body()
                        if (data != null && data.isNotEmpty())
                            mp4 = JSONObject(data).getJSONObject("src").getString("src")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }
}
