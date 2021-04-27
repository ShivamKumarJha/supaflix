package com.shivamkumarjha.supaflix.ui.videoplayer

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.shivamkumarjha.supaflix.ui.videoplayer.util.FlowDebouncer
import com.shivamkumarjha.supaflix.ui.videoplayer.util.set
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

internal class DefaultVideoPlayerController(
    private val context: Context,
    private val initialState: VideoPlayerState,
    private val coroutineScope: CoroutineScope
) : VideoPlayerController {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<VideoPlayerState>
        get() = _state.asStateFlow()

    fun <T> currentState(filter: (VideoPlayerState) -> T): T {
        return filter(_state.value)
    }

    @Composable
    fun collect(): State<VideoPlayerState> {
        return _state.collectAsState()
    }

    @Composable
    fun <T> collect(filter: VideoPlayerState.() -> T): State<T> {
        return remember(filter) {
            _state.map { it.filter() }
        }.collectAsState(
            initial = _state.value.filter()
        )
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    var videoPlayerBackgroundColor: Int = DefaultVideoPlayerBackgroundColor.value.toInt()
        set(value) {
            field = value
            playerView?.setBackgroundColor(value)
        }

    private lateinit var videoPlayerSource: VideoPlayerSource
    private var playerView: PlayerView? = null

    private var updateDurationAndPositionJob: Job? = null
    private val playerEventListener = object : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (PlaybackState.of(playbackState) == PlaybackState.READY) {
                updateDurationAndPositionJob?.cancel()
                updateDurationAndPositionJob = coroutineScope.launch {
                    while (this.isActive) {
                        updateDurationAndPosition()
                        delay(250)
                    }
                }
            }

            _state.set {
                copy(
                    isPlaying = playWhenReady,
                    playbackState = PlaybackState.of(playbackState)
                )
            }
        }
    }

    private val videoListener = object : VideoListener {
        override fun onVideoSizeChanged(
            width: Int,
            height: Int,
            unappliedRotationDegrees: Int,
            pixelWidthHeightRatio: Float
        ) {
            super.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)

            _state.set {
                copy(videoSize = width.toFloat() to height.toFloat())
            }
        }
    }

    /**
     * Internal exoPlayer instance
     */
    private val exoPlayer = SimpleExoPlayer.Builder(context)
        .build()
        .apply {
            playWhenReady = initialState.isPlaying
            repeatMode = Player.REPEAT_MODE_ONE
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            addListener(playerEventListener)
            addVideoListener(videoListener)
        }

    /**
     * Not so efficient way of showing preview in video slider.
     */
    private val previewExoPlayer = SimpleExoPlayer.Builder(context)
        .build()
        .apply {
            playWhenReady = false
        }

    private val previewSeekDebouncer = FlowDebouncer<Long>(200L)

    init {
        exoPlayer.playWhenReady = initialState.isPlaying

        coroutineScope.launch {
            previewSeekDebouncer.collect { position ->
                previewExoPlayer.seekTo(position)
            }
        }
    }

    /**
     * A flag to indicate whether source is already set and waiting for
     * playerView to become available.
     */
    private val waitPlayerViewToPrepare = AtomicBoolean(false)

    override fun play() {
        if (exoPlayer.playbackState == Player.STATE_ENDED) {
            exoPlayer.seekTo(0)
        }
        videoPlayerSource.viewModel.addToHistory(videoPlayerSource.history)
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
        updateDb()
    }

    override fun playPauseToggle() {
        if (exoPlayer.isPlaying) pause()
        else play()
    }

    override fun quickSeekForward() {
        if (_state.value.quickSeekAction.direction != QuickSeekDirection.None) {
            // Currently animating
            return
        }
        val target = (exoPlayer.currentPosition + 10_000).coerceAtMost(exoPlayer.duration)
        exoPlayer.seekTo(target)
        updateDurationAndPosition()
        _state.set { copy(quickSeekAction = QuickSeekAction.forward()) }
    }

    override fun quickSeekRewind() {
        if (_state.value.quickSeekAction.direction != QuickSeekDirection.None) {
            // Currently animating
            return
        }
        val target = (exoPlayer.currentPosition - 10_000).coerceAtLeast(0)
        exoPlayer.seekTo(target)
        updateDurationAndPosition()
        _state.set { copy(quickSeekAction = QuickSeekAction.rewind()) }
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        updateDurationAndPosition()
        updateDb()
    }

    override fun setSource(videoPlayerSource: VideoPlayerSource) {
        this.videoPlayerSource = videoPlayerSource
        if (playerView == null) {
            waitPlayerViewToPrepare.set(true)
        } else {
            prepare()
        }
    }

    private fun updateDb() {
        videoPlayerSource.history.position = exoPlayer.currentPosition
        videoPlayerSource.history.duration = exoPlayer.duration
        videoPlayerSource.viewModel.updateHistory(videoPlayerSource.history)
    }

    fun enableGestures(isEnabled: Boolean) {
        _state.set { copy(gesturesEnabled = isEnabled) }
    }

    fun enableControls(enabled: Boolean) {
        _state.set { copy(controlsEnabled = enabled) }
    }

    fun showControls() {
        _state.set { copy(controlsVisible = true) }
    }

    fun hideControls() {
        _state.set { copy(controlsVisible = false) }
    }

    fun setDraggingProgress(draggingProgress: DraggingProgress?) {
        _state.set { copy(draggingProgress = draggingProgress) }
    }

    fun setQuickSeekAction(quickSeekAction: QuickSeekAction) {
        _state.set { copy(quickSeekAction = quickSeekAction) }
    }

    private fun updateDurationAndPosition() {
        _state.set {
            copy(
                duration = exoPlayer.duration.coerceAtLeast(0),
                currentPosition = exoPlayer.currentPosition.coerceAtLeast(0),
                secondaryProgress = exoPlayer.bufferedPosition.coerceAtLeast(0)
            )
        }
    }

    private fun prepare() {
        fun createVideoSource(): MediaSource {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.packageName)
            )

            val mediaSource =
                if (videoPlayerSource.type.toLowerCase(Locale.ROOT).contains("dash") ||
                    videoPlayerSource.type.toLowerCase(Locale.ROOT).contains("m3u8") ||
                    videoPlayerSource.type.toLowerCase(Locale.ROOT).contains("hls")
                ) {
                    HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(videoPlayerSource.url)))
                } else {
                    ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(videoPlayerSource.url)))
                }

            if (videoPlayerSource.subtitleUrl != null) {
                val mimeTypes = if (videoPlayerSource.subtitleUrl!!.contains(".vtt"))
                    MimeTypes.TEXT_VTT
                else
                    MimeTypes.APPLICATION_SUBRIP
                val sub: MediaItem.Subtitle = MediaItem.Subtitle(
                    Uri.parse(videoPlayerSource.subtitleUrl),
                    mimeTypes,
                    "en"
                )
                val textMediaSource: MediaSource =
                    SingleSampleMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(sub, exoPlayer.duration)
                return MergingMediaSource(mediaSource, textMediaSource)
            }
            return mediaSource
        }
        exoPlayer.setMediaSource(createVideoSource())
        previewExoPlayer.setMediaSource(createVideoSource())

        exoPlayer.prepare()
        previewExoPlayer.prepare()

        exoPlayer.seekTo(videoPlayerSource.history.window, videoPlayerSource.history.position)
    }

    fun playerViewAvailable(playerView: PlayerView) {
        this.playerView = playerView
        playerView.player = exoPlayer
        playerView.setBackgroundColor(videoPlayerBackgroundColor)

        if (waitPlayerViewToPrepare.compareAndSet(true, false)) {
            prepare()
        }
    }

    fun previewPlayerViewAvailable(playerView: PlayerView) {
        playerView.player = previewExoPlayer
    }

    fun previewSeekTo(position: Long) {
        // position is very accurate. Thumbnail doesn't have to be.
        // Roll to the nearest "even" integer.
        val seconds = position.toInt() / 1000
        val nearestEven = (seconds - seconds.rem(2)).toLong()
        coroutineScope.launch {
            previewSeekDebouncer.put(nearestEven * 1000)
        }
    }

    override fun reset() {
        exoPlayer.stop()
        previewExoPlayer.stop()
    }
}

val DefaultVideoPlayerBackgroundColor = Color.Black