package com.shivamkumarjha.supaflix.ui.videoplayer

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.shivamkumarjha.supaflix.ui.player.PlayerInteractionEvents
import com.shivamkumarjha.supaflix.ui.videoplayer.util.getDurationString

@Composable
fun MediaControlButtons(videoPlayerSource: VideoPlayerSource, modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    val controlsEnabled by controller.collect { controlsEnabled }

    // Dictates the direction of appear animation.
    // If controlsVisible is true, appear animation needs to be triggered.
    val controlsVisible by controller.collect { controlsVisible }

    // When controls are not visible anymore we should remove them from UI tree
    // Controls by default should always be on screen.
    // Only when disappear animation finishes, controls can be freely cleared from the tree.
    val (controlsExistOnUITree, setControlsExistOnUITree) = remember(controlsVisible) {
        mutableStateOf(true)
    }

    val appearAlpha = remember { Animatable(0f) }

    LaunchedEffect(controlsVisible) {
        appearAlpha.animateTo(
            targetValue = if (controlsVisible) 1f else 0f,
            animationSpec = tween(
                durationMillis = 250,
                easing = LinearEasing
            )
        )
        setControlsExistOnUITree(controlsVisible)
    }

    if (controlsEnabled && controlsExistOnUITree) {
        MediaControlButtonsContent(
            videoPlayerSource,
            modifier = Modifier
                .alpha(appearAlpha.value)
                .background(Color.Black.copy(alpha = appearAlpha.value * 0.6f))
                .then(modifier)
        )
    }
}

@Composable
private fun MediaControlButtonsContent(
    videoPlayerSource: VideoPlayerSource,
    modifier: Modifier = Modifier
) {
    val controller = LocalVideoPlayerController.current

    Box(modifier = modifier
        .fillMaxSize()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            controller.hideControls()
        }) {
        IconButton(onClick = {
            videoPlayerSource.interactionEvents(PlayerInteractionEvents.NavigateUp(false))
        }, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        TopButtons(videoPlayerSource, modifier = Modifier.align(Alignment.TopEnd))
        PlayerControls(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun PlayerControls(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PositionAndDurationNumbers(modifier = Modifier.fillMaxWidth())
        ProgressIndicator(modifier = Modifier.fillMaxWidth())
        PlayPauseButton(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PositionAndDurationNumbers(modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    val positionText by controller.collect {
        getDurationString(currentPosition, false)
    }
    val remainingDurationText by controller.collect {
        getDurationString(duration - currentPosition, false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            positionText,
            style = TextStyle(
                shadow = Shadow(
                    blurRadius = 8f,
                    offset = Offset(2f, 2f)
                )
            )
        )
        Box(modifier = Modifier.weight(1f))
        Text(
            remainingDurationText,
            style = TextStyle(
                shadow = Shadow(
                    blurRadius = 8f,
                    offset = Offset(2f, 2f)
                )
            )
        )
    }
}

@Composable
fun PlayPauseButton(modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    val isPlaying by controller.collect { isPlaying }
    val playbackState by controller.collect { playbackState }

    IconButton(
        onClick = { controller.playPauseToggle() },
        modifier = modifier
    ) {
        if (isPlaying) {
            ShadowedIcon(icon = Icons.Filled.Pause)
        } else {
            when (playbackState) {
                PlaybackState.ENDED -> {
                    ShadowedIcon(icon = Icons.Filled.Restore)
                }
                PlaybackState.BUFFERING -> {
                    CircularProgressIndicator()
                }
                else -> {
                    ShadowedIcon(icon = Icons.Filled.PlayArrow)
                }
            }
        }
    }
}

@Composable
private fun TopButtons(videoPlayerSource: VideoPlayerSource, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    Row(modifier = modifier) {
        IconButton(onClick = {
            videoPlayerSource.interactionEvents(
                PlayerInteractionEvents.DownloadVideo(
                    videoPlayerSource.url,
                    videoPlayerSource.type,
                    videoPlayerSource.history
                )
            )
        }, modifier = Modifier.padding(end = 16.dp)) {
            Icon(
                imageVector = Icons.Filled.Download,
                contentDescription = null
            )
        }
        IconButton(onClick = {
            videoPlayerSource.interactionEvents(
                PlayerInteractionEvents.ToggleOrientation(
                    configuration.orientation
                )
            )
        }) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    Icon(
                        imageVector = Icons.Filled.ScreenLockPortrait,
                        contentDescription = null
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Filled.ScreenLockLandscape,
                        contentDescription = null
                    )
                }
            }
        }
    }
}