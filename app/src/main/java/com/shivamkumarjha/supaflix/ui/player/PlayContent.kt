package com.shivamkumarjha.supaflix.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.network.Resource

@Composable
fun PlayContent(
    history: History,
    interactionEvents: (PlayerInteractionEvents) -> Unit
) {
    val viewModel: PlayerViewModel = viewModel()
    val browserLink = viewModel.browserLink.observeAsState(null)
    val fcdn = viewModel.fcdn.observeAsState(Resource.loading(null))
    val gocdn = viewModel.gocdn.observeAsState(Resource.loading(null))
    val movCloud = viewModel.movCloud.observeAsState(Resource.loading(null))
    val vidCloud = viewModel.vidCloud.observeAsState(Resource.loading(null))
    remember {
        viewModel.embeds(history)
        true
    }

    if (browserLink.value != null) {
        viewModel.addToHistory(history)
        interactionEvents(PlayerInteractionEvents.OpenBrowser(browserLink.value!!))
    }
    if (!vidCloud.value.data?.source.isNullOrEmpty()) {
        viewModel.addToHistory(history)
        val subtitle =
            if (vidCloud.value.data!!.track != null && !vidCloud.value.data!!.track?.tracks.isNullOrEmpty())
                vidCloud.value.data!!.track!!.tracks.first().file
            else
                null
        interactionEvents(
            PlayerInteractionEvents.OpenPlayer(
                vidCloud.value.data!!.source!!.first().file,
                vidCloud.value.data!!.source!!.first().type,
                subtitle
            )
        )
    } else if (!vidCloud.value.data?.linkiframe.isNullOrBlank()) {
        if (vidCloud.value.data!!.linkiframe!!.contains("https://movcloud.net/embed/")) {
            viewModel.getMovCloudLink(vidCloud.value.data!!.linkiframe!!)
        } else {
            interactionEvents(PlayerInteractionEvents.OpenBrowser(vidCloud.value.data!!.linkiframe!!))
        }
    }
    if (!fcdn.value.data?.data.isNullOrEmpty()) {
        viewModel.addToHistory(history)
        PlayerContent(fcdn.value.data!!.data.first().file, fcdn.value.data!!.data.first().type)
    }
    if (!gocdn.value.data?.sources.isNullOrEmpty()) {
        viewModel.addToHistory(history)
        PlayerContent(
            gocdn.value.data!!.sources.first().file,
            gocdn.value.data!!.sources.first().type
        )
    }
    if (!movCloud.value.data?.data?.sources.isNullOrEmpty()) {
        viewModel.addToHistory(history)
        PlayerContent(movCloud.value.data!!.data.sources.first().file, "hls")
    }
}