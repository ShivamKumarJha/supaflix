package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Base64
import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class UrlResolver {

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

    fun getFinalURL(url: String): String? {
        return when {
            url.contains("clipwatching") -> ClipWatching.getFasterLink(url)
            url.contains("cloudvideo") -> CloudVideo.getFasterLink(url)
            url.contains("dood") -> Dood.getFasterLink(url)
            url.contains("fembed") -> Fembed.getFasterLink(url)
            url.contains("jawcloud") -> JawCloud.getFasterLink(url)
            url.contains("jetload") -> JetLoad.getFasterLink(url)
            url.contains("mixdrop") -> MixDrop.getFasterLink(url)
            url.contains("mp4upload") -> Mp4Upload.getFasterLink(url)
            url.contains("openplay") -> OpenPlay.getFasterLink(url)
            url.contains("prostream") -> ProStream.getFasterLink(url)
            url.contains("streamtape") -> StreamTape.getFasterLink(url)
            url.contains("supervideo") -> SuperVideo.getFasterLink(url)
            url.contains("upstream") -> UpStream.getFasterLink(url)
            url.contains("uptostream") -> UpToStream.getFasterLink(url)
            url.contains("uqload") -> UqLoad.getFasterLink(url)
            url.contains("veoh") -> Veoh.getFasterLink(url)
            url.contains("vidcloud") -> VidCloud.getFasterLink(url)
            url.contains("videobin") -> VideoBin.getFasterLink(url)
            url.contains("videomega") -> VideoMega.getFasterLink(url)
            url.contains("vidfast") -> VidFast.getFasterLink(url)
            url.contains("vidia") -> Vidia.getFasterLink(url)
            url.contains("vidlox") -> Vidlox.getFasterLink(url)
            url.contains("vidoza") -> Vidoza.getFasterLink(url)
            url.contains("vup") -> Vup.getFasterLink(url)
            else -> null
        }
    }
}