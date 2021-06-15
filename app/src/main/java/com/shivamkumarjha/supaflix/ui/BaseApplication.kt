package com.shivamkumarjha.supaflix.ui

import android.app.Application
import com.github.javiersantos.piracychecker.utils.apkSignatures
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        var AUTH: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        AUTH = getAuthString()
    }

    private fun getAuthString(): String {
        val skk = applicationContext.apkSignatures[0]
        val auth = ""
        return "{\"skk\":\"$skk\",\"auth\":\"$auth\"}"
    }
}