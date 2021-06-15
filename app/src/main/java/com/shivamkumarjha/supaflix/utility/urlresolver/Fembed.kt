package com.shivamkumarjha.supaflix.utility.urlresolver

import android.util.Log
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver.Companion.TIMEOUT_EXTRACT_MILS
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


object Fembed {

    fun getFasterLink(l: String): String? {
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
}
