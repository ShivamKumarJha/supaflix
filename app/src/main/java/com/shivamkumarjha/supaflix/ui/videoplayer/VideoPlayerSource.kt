package com.shivamkumarjha.supaflix.ui.videoplayer

import androidx.annotation.RawRes

sealed class VideoPlayerSource {
    data class Raw(@RawRes val resId: Int) : VideoPlayerSource()
    data class Network(val url: String, val headers: Map<String, String> = mapOf()) :
        VideoPlayerSource()
}