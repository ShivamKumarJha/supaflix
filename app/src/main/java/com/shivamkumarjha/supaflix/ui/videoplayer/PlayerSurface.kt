package com.shivamkumarjha.supaflix.ui.videoplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun PlayerSurface(
    modifier: Modifier = Modifier,
    onPlayerViewAvailable: (PlayerView) -> Unit = {}
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                keepScreenOn = true
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                onPlayerViewAvailable(this)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}