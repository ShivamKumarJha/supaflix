package com.shivamkumarjha.supaflix.utility

import android.content.Context
import android.util.Base64
import android.util.Log
import com.github.javiersantos.piracychecker.utils.apkSignatures
import com.shivamkumarjha.supaflix.config.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

class UrlResolver(private val context: Context) {

    companion object {
        const val API_EXTRACTOR = "https://nodeurlresolver.herokuapp.com/api/v1/"

        val SUPPORTED_HOSTS = listOf(
            "clipwatching",
            "cloudvideo",
            "dood",
            "fembed",
            "jawcloud",
            "jetload",
            "mixdrop",
            "mp4upload",
            "openlay",
            "prostream",
            "streamtape",
            "supervideo",
            "upstream",
            "uptostream",
            "uqload",
            "veoh",
            "vidcloud",
            "videobin",
            "videomega",
            "vidfast",
            "vidia",
            "vidlox",
            "vidoza",
            "vup"
        )

        val DOWNLOADABLE_HOSTS = listOf(
            "clipwatching",
            "cloudvideo",
            "fembed",
            "jawcloud",
            "jetload",
            "mp4upload",
            "openlay",
            "prostream",
            "streamtape",
            "supervideo",
            "upstream",
            "uptostream",
            "uqload",
            "veoh",
            "vidcloud",
            "videobin",
            "videomega",
            "vidfast",
            "vidia",
            "vidlox",
            "vidoza",
            "vup"
        )

        const val TIMEOUT_EXTRACT_MILS = 20000
        const val TIMEOUT_SECONDS = 8
    }

    fun isSupportedHost(url: String): Boolean {
        for (host in SUPPORTED_HOSTS) if (url.contains(host)) return true
        return false
    }

    fun isDownloadableHost(url: String): Boolean {
        for (host in DOWNLOADABLE_HOSTS) if (url.contains(host)) return true
        return false
    }

    fun encodeMSG(msg: String): String {
        val data = msg.toByteArray(StandardCharsets.UTF_8)
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    private fun urlEncodeUTF8(s: String): String? {
        return try {
            URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.e(Constants.TAG, e.message ?: "Error urlEncodeUTF8")
            return null
        }
    }

    fun urlEncodeUTF8(map: Map<*, *>): String {
        val sb = StringBuilder()
        for ((key, value) in map) {
            if (sb.isNotEmpty()) {
                sb.append("&")
            }
            sb.append(
                String.format(
                    "%s=%s",
                    urlEncodeUTF8(key.toString()),
                    urlEncodeUTF8(value.toString())
                )
            )
        }
        return sb.toString()
    }

    fun md5(s: String): String? {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuffer()
            for (i in messageDigest.indices) hexString.append(
                Integer.toHexString(
                    0xFF and messageDigest[i]
                        .toInt()
                )
            )
            hexString.toString()
        } catch (e: Exception) {
            encodeMSG(s)
        }
    }

    fun getCheckString(): String {
        val skk = context.apkSignatures[0]
        val auth = ""
        return "{\"skk\":\"$skk\",\"auth\":\"$auth\"}"
    }

    suspend fun getFinalURL(scope: CoroutineScope, url: String): String? {
        return scope.async(Dispatchers.IO) {
            when {
                url.contains("clipwatching") -> clipWatching(url)
                url.contains("cloudvideo") -> cloudVideo(url)
                url.contains("dood") -> dood(url)
                url.contains("fembed") -> fembed(url)
                url.contains("jawcloud") -> jawCloud(url)
                url.contains("jetload") -> jetLoad(url)
                url.contains("mixdrop") -> mixDrop(url)
                url.contains("mp4upload") -> mp4Upload(url)
                url.contains("openplay") -> openPlay(url)
                url.contains("prostream") -> proStream(url)
                url.contains("streamtape") -> streamTape(url)
                url.contains("supervideo") -> superVideo(url)
                url.contains("upstream") -> upStream(url)
                url.contains("uptostream") -> upToStream(url)
                url.contains("uqload") -> uqLoad(url)
                url.contains("veoh") -> veoh(url)
                url.contains("vidcloud") -> vidCloud(url)
                url.contains("videobin") -> videoBin(url)
                url.contains("videomega") -> videoMega(url)
                url.contains("vidfast") -> vidFast(url)
                url.contains("vidia") -> vidia(url)
                url.contains("vidlox") -> vidlox(url)
                url.contains("vidoza") -> vidoza(url)
                url.contains("vup") -> vup(url)
                else -> null
            }
        }.await()
    }

    private fun clipWatching(l: String): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "clipwatching"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun cloudVideo(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        link = if (link.contains("/embed-")) link else "https://cloudvideo.tv/embed-" +
                link.replace(".html", "").split("/".toRegex()).toTypedArray()[3] + ".html"
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                //
                val apiURL: String = API_EXTRACTOR + "cloudvideo"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun dood(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
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
                            .data("auth", encodeMSG(authJSON))
                            .data("source", encodeMSG(document.toString()))
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
                                .data("auth", encodeMSG(authJSON))
                                .data("source", encodeMSG(document.toString()))
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

    private fun fembed(l: String): String? {
        var mp4: String? = null
        val file = l.split("/".toRegex()).toTypedArray()[4]
        try {
            val apiURL = "https://feurl.com/api/source/$file"
            val obj = Jsoup.connect(apiURL)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .data("r", "")
                .data("d", "feurl.com")
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body()
            try {
                val json = JSONObject(obj)
                if (json.getBoolean("success") && obj.contains("mp4")) {
                    val jsonArray = json.getJSONArray("data")
                    mp4 = jsonArray.getJSONObject(0).getString("file")
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, e.message ?: "Error")
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun jawCloud(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link =
            if (link.contains("/embed-")) link else "https://jawcloud.co/embed-" + link.split("/".toRegex())
                .toTypedArray()[3]
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "jawcloud"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun jetLoad(l: String): String? {
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
                val post: String? = urlEncodeUTF8(params)
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

    private fun mixDrop(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
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
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun mp4Upload(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link = if (link.contains("/embed-")) link else "https://www.mp4upload.com/embed-" +
                link.split("/".toRegex()).toTypedArray()[3].replace(".html", "") + ".html"
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "mp4upload"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun openPlay(l: String?): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "openplay"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun proStream(l: String?): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "prostream"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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

    private fun streamTape(l: String?): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "streamtape"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun superVideo(l: String?): String? {
        if (l == null)
            return null
        val authJSON: String = getCheckString()
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
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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
                        .data("source", encodeMSG(l))
                        .data("auth", encodeMSG(authJSON))
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

    private fun upStream(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link =
            if (link.contains("/embed-")) link else "https://upstream.to/embed-" + link.split("/".toRegex())
                .toTypedArray()[3] + ".html"
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "upstream"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun upToStream(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
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
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document))
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

    private fun uqLoad(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link =
            if (link.contains("/embed-")) link else "https://uqload.com/embed-" + link.split("/".toRegex())
                .toTypedArray()[3]
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "uqload"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun veoh(l: String): String? {
        val authJSON: String = getCheckString()
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
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document))
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

    private fun vidCloud(l: String): String? {
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
                val post: String = urlEncodeUTF8(params)
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

    private fun videoBin(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link =
            if (link.contains("/embed-")) link else "https://videobin.co/embed-" + link.split("/".toRegex())
                .toTypedArray()[3]
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "videobin"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun videoMega(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        link =
            if (link.contains("/e/")) link else "https://www.videomega.co/e/" + link.split("/".toRegex())
                .toTypedArray()[3]
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "videomega"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute().body()
                if (obj != null && obj.contains("url")) {
                    val json = JSONObject(obj)
                    if (json.getString("status") == "ok") mp4 = json.getString("url")
                }
            } catch (e: java.lang.Exception) {
                Log.e(Constants.TAG, e.message ?: "Error")
            }
        } catch (e: java.lang.Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun vidFast(l: String): String? {
        var link = l
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        link =
            if (link.contains("/embed-")) link else "https://vidfast.co/embed-" + link.split("/".toRegex())
                .toTypedArray()[3].replace(".html", "") + ".html"
        try {
            document = Jsoup.connect(link)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "vidfast"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun vidia(l: String?): String? {
        if (l == null)
            return null

        val authJSON: String = getCheckString()
        var mp4: String? = null
        try {
            val apiURL: String = API_EXTRACTOR + "vidia"
            val obj = Jsoup.connect(apiURL)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .data("source", encodeMSG(l))
                .data("auth", encodeMSG(authJSON))
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

    private fun vidlox(l: String?): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "vidlox"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message ?: "Error")
        }
        return mp4
    }

    private fun vidoza(l: String?): String? {
        val authJSON: String = getCheckString()
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
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", encodeMSG(authJSON))
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

    private fun vup(l: String?): String? {
        val authJSON: String = getCheckString()
        val document: Document?
        var mp4: String? = null
        try {
            document = Jsoup.connect(l)
                .timeout(TIMEOUT_EXTRACT_MILS)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                val apiURL: String = API_EXTRACTOR + "vup"
                val obj = Jsoup.connect(apiURL)
                    .timeout(TIMEOUT_EXTRACT_MILS)
                    .data("mode", "local")
                    .data("auth", encodeMSG(authJSON))
                    .data("source", encodeMSG(document.toString()))
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