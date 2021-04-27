package com.shivamkumarjha.supaflix.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Embeds
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.ui.detail.ShowProgressBar
import com.shivamkumarjha.supaflix.ui.theme.ThemeUtility
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource
import java.util.*

@Composable
fun PlayContent(
    history: History,
    interactionEvents: (PlayerInteractionEvents) -> Unit
) {
    val viewModel: PlayerViewModel = viewModel()
    val error = viewModel.error.observeAsState(false)
    val browserLink = viewModel.browserLink.observeAsState(null)
    val fcdn = viewModel.fcdn.observeAsState(Resource.loading(null))
    val gocdn = viewModel.gocdn.observeAsState(Resource.loading(null))
    val movCloud = viewModel.movCloud.observeAsState(Resource.loading(null))
    val vidCloud = viewModel.vidCloud.observeAsState(Resource.loading(null))
    val linkFrame = viewModel.linkFrame.observeAsState(Resource.loading(null))
    val serverList = viewModel.serverList.observeAsState(null)
    remember {
        viewModel.embeds(history)
        true
    }

    if (error.value) {
        interactionEvents(PlayerInteractionEvents.NavigateUp)
    }
    if (browserLink.value != null) {
        viewModel.addToHistory(history)
        interactionEvents(PlayerInteractionEvents.OpenBrowser(browserLink.value!!))
    }
    if (!fcdn.value.data?.data.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            fcdn.value.data!!.data.first().file,
            fcdn.value.data!!.data.first().type,
            null,
            history,
            viewModel
        )
        PlayerContent(videoPlayerSource)
    }
    if (!gocdn.value.data?.sources.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            gocdn.value.data!!.sources.first().file,
            gocdn.value.data!!.sources.first().type,
            null,
            history,
            viewModel
        )
        PlayerContent(videoPlayerSource)
    }
    if (!movCloud.value.data?.data?.sources.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            movCloud.value.data!!.data.sources.first().file,
            "hls",
            null,
            history,
            viewModel
        )
        PlayerContent(videoPlayerSource)
    }
    if (!vidCloud.value.data?.source.isNullOrEmpty()) {
        val subtitle =
            if (vidCloud.value.data!!.track != null && !vidCloud.value.data!!.track?.tracks.isNullOrEmpty())
                vidCloud.value.data!!.track!!.tracks.first().file
            else
                null
        val videoPlayerSource = VideoPlayerSource(
            vidCloud.value.data!!.source!!.first().file,
            vidCloud.value.data!!.source!!.first().type,
            subtitle,
            history,
            viewModel
        )
        interactionEvents(PlayerInteractionEvents.OpenPlayer(videoPlayerSource))
    } else if (!vidCloud.value.data?.linkiframe.isNullOrBlank()) {
        if (vidCloud.value.data!!.linkiframe!!.contains("https://movcloud.net/embed/")) {
            viewModel.getMovCloudLink(vidCloud.value.data!!.linkiframe!!)
        } else {
            interactionEvents(PlayerInteractionEvents.OpenBrowser(vidCloud.value.data!!.linkiframe!!))
        }
    }
    if (!linkFrame.value.data?.source.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            linkFrame.value.data!!.source.first().file,
            linkFrame.value.data!!.source.first().type,
            null,
            history,
            viewModel
        )
        interactionEvents(PlayerInteractionEvents.OpenPlayer(videoPlayerSource))
    } else if (!linkFrame.value.data?.linkiframe.isNullOrBlank()) {
        if (linkFrame.value.data!!.linkiframe!!.contains("https://movcloud.net/embed/")) {
            viewModel.getMovCloudLink(linkFrame.value.data!!.linkiframe!!)
        } else {
            interactionEvents(PlayerInteractionEvents.OpenBrowser(linkFrame.value.data!!.linkiframe!!))
        }
    }
    if (!serverList.value.isNullOrEmpty()) {
        ServerPicker(viewModel, history, serverList.value!!)
    } else {
        ShowProgressBar()
    }
}

@Composable
fun ServerPicker(viewModel: PlayerViewModel, history: History, servers: List<Embeds>) {
    val recommendedServers: ArrayList<Embeds> = arrayListOf()
    val otherServers: ArrayList<Embeds> = arrayListOf()
    for (server in servers) {
        when {
            server.part_of.toLowerCase(Locale.ROOT).contains("vidcloud") ->
                recommendedServers.add(server)
            server.part_of.toLowerCase(Locale.ROOT).contains("fcdn") ->
                recommendedServers.add(server)
            server.part_of.toLowerCase(Locale.ROOT).contains("movcloud") ->
                recommendedServers.add(server)
            server.part_of.toLowerCase(Locale.ROOT).contains("mega") ->
                recommendedServers.add(server)
            else -> otherServers.add(server)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeUtility.surfaceBackground(isSystemInDarkTheme())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!recommendedServers.isNullOrEmpty()) {
            Text(
                text = stringResource(id = R.string.recommended_server),
                modifier = Modifier.padding(8.dp),
                color = ThemeUtility.textColor(isSystemInDarkTheme()),
                style = typography.h6
            )
            LazyColumn {
                items(recommendedServers) { server ->
                    Button(
                        onClick = {
                            viewModel.server(history, server.hash)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = server.server,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
        if (!otherServers.isNullOrEmpty()) {
            Text(
                text = stringResource(id = R.string.other_server),
                modifier = Modifier.padding(8.dp),
                color = ThemeUtility.textColor(isSystemInDarkTheme()),
                style = typography.h6
            )
            LazyColumn {
                items(otherServers) { server ->
                    Button(
                        onClick = {
                            viewModel.server(history, server.hash)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = server.server,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}