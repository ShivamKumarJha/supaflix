package com.shivamkumarjha.supaflix.persistence

import android.content.Context
import android.content.SharedPreferences
import com.shivamkumarjha.supaflix.config.Constants

class PreferenceManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun getHistoryId(): Int {
        val id = pref.getInt(Constants.PREF_HISTORY_ID, 0) + 1
        pref.edit().putInt(Constants.PREF_HISTORY_ID, id).apply()
        return id
    }

    var autoServerPick: Boolean
        get() = pref.getBoolean(Constants.PREF_SHOW_SERVER_PICKER, true)
        set(serverPicker) {
            pref.edit().putBoolean(Constants.PREF_SHOW_SERVER_PICKER, serverPicker).apply()
        }

    var showSubtitles: Boolean
        get() = pref.getBoolean(Constants.PREF_SHOW_SUBTITLES, true)
        set(showSubtitles) {
            pref.edit().putBoolean(Constants.PREF_SHOW_SUBTITLES, showSubtitles).apply()
        }

    var landscapePlayer: Boolean
        get() = pref.getBoolean(Constants.PREF_LANDSCAPE_PLAYER, true)
        set(landscapePlayer) {
            pref.edit().putBoolean(Constants.PREF_LANDSCAPE_PLAYER, landscapePlayer).apply()
        }
}