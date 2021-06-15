package com.shivamkumarjha.supaflix.ui.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import com.shivamkumarjha.supaflix.ui.videoplayer.VideoPlayerSource
import com.shivamkumarjha.supaflix.utility.urlresolver.UrlResolver
import java.util.*

@Composable
fun PlayContent(
    history: History,
    interactionEvents: (PlayerInteractionEvents) -> Unit
) {
    val viewModel: PlayerViewModel = viewModel()
    val error = viewModel.error.observeAsState(false)
    val browserLink = viewModel.browserLink.observeAsState(null)
    val resolverLink = viewModel.resolverLink.observeAsState(null)
    val fcdn = viewModel.fcdn.observeAsState(Resource.loading(null))
    val gocdn = viewModel.gocdn.observeAsState(Resource.loading(null))
    val movCloud = viewModel.movCloud.observeAsState(Resource.loading(null))
    val vidCloud = viewModel.vidCloud.observeAsState(Resource.loading(null))
    val linkFrame = viewModel.linkFrame.observeAsState(Resource.loading(null))
    val serverList = viewModel.serverList.observeAsState(null)

    LaunchedEffect(key1 = history, block = {
        viewModel.embeds(history)
    })

    if (error.value) {
        interactionEvents(PlayerInteractionEvents.NavigateUp(true))
    }
    if (browserLink.value != null) {
        viewModel.addToHistory(history)
        interactionEvents(PlayerInteractionEvents.OpenBrowser(browserLink.value!!))
    }
    if (resolverLink.value != null) {
        val videoPlayerSource = VideoPlayerSource(
            resolverLink.value!!,
            "mp4",
            null,
            history,
            viewModel,
            interactionEvents
        )
        PlayerContent(videoPlayerSource)
    }
    if (!fcdn.value.data?.data.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            fcdn.value.data!!.data.first().file,
            fcdn.value.data!!.data.first().type,
            null,
            history,
            viewModel,
            interactionEvents
        )
        PlayerContent(videoPlayerSource)
    }
    if (!gocdn.value.data?.sources.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            gocdn.value.data!!.sources.first().file,
            gocdn.value.data!!.sources.first().type,
            null,
            history,
            viewModel,
            interactionEvents
        )
        PlayerContent(videoPlayerSource)
    }
    if (!movCloud.value.data?.data?.sources.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            movCloud.value.data!!.data.sources.first().file,
            "hls",
            null,
            history,
            viewModel,
            interactionEvents
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
            viewModel,
            interactionEvents
        )
        interactionEvents(PlayerInteractionEvents.OpenPlayer(videoPlayerSource))
    } else if (!vidCloud.value.data?.linkiframe.isNullOrBlank()) {
        if (vidCloud.value.data!!.linkiframe!!.contains("https://movcloud.net/embed/")) {
            viewModel.getMovCloudLink(vidCloud.value.data!!.linkiframe!!)
        } else {
            viewModel.addToHistory(history)
            interactionEvents(PlayerInteractionEvents.OpenBrowser(vidCloud.value.data!!.linkiframe!!))
        }
    }
    if (!linkFrame.value.data?.source.isNullOrEmpty()) {
        val videoPlayerSource = VideoPlayerSource(
            linkFrame.value.data!!.source.first().file,
            linkFrame.value.data!!.source.first().type,
            null,
            history,
            viewModel,
            interactionEvents
        )
        interactionEvents(PlayerInteractionEvents.OpenPlayer(videoPlayerSource))
    } else if (!linkFrame.value.data?.linkiframe.isNullOrBlank()) {
        if (linkFrame.value.data!!.linkiframe!!.contains("https://movcloud.net/embed/")) {
            viewModel.getMovCloudLink(linkFrame.value.data!!.linkiframe!!)
        } else {
            viewModel.addToHistory(history)
            interactionEvents(PlayerInteractionEvents.OpenBrowser(linkFrame.value.data!!.linkiframe!!))
        }
    }
    if (!serverList.value.isNullOrEmpty() && browserLink.value == null && resolverLink.value == null) {
        ServerPicker(viewModel, history, serverList.value!!)
    } else {
        ShowProgressBar()
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ServerPicker(viewModel: PlayerViewModel, history: History, servers: List<Embeds>) {
    val urlResolver = UrlResolver()
    val recommendedServers: ArrayList<Embeds> = arrayListOf()
    val otherServers: ArrayList<Embeds> = arrayListOf()
    for (server in servers) {
        when {
            server.part_of.equals("vidcloud", ignoreCase = true) -> recommendedServers.add(server)
            server.part_of.equals("fcdn", ignoreCase = true) -> recommendedServers.add(server)
            server.part_of.equals("movcloud", ignoreCase = true) -> recommendedServers.add(server)
            server.part_of.equals("mega", ignoreCase = true) -> recommendedServers.add(server)
            urlResolver.isSupportedHost(server.part_of.lowercase(Locale.ENGLISH)) -> recommendedServers.add(
                server
            )
            else -> otherServers.add(server)
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!recommendedServers.isNullOrEmpty()) {
            stickyHeader {
                Text(
                    text = stringResource(id = R.string.recommended_server),
                    modifier = Modifier.padding(8.dp),
                    style = typography.h6
                )
            }
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
        if (!otherServers.isNullOrEmpty()) {
            stickyHeader {
                Text(
                    text = stringResource(id = R.string.other_server),
                    modifier = Modifier.padding(8.dp),
                    style = typography.h6
                )
            }
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