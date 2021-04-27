package com.shivamkumarjha.supaflix.ui.videoplayer.util

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.set(block: T.() -> T) {
    this.value = this.value.block()
}