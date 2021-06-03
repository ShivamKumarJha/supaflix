package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.History
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun HistoryContent(
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            HistoryColumns(listState, interactionEvents, viewModel, Modifier.fillMaxSize())

            // Show the jump button
            val jumpVisibility by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex != 0
                }
            }

            JumpToPosition(
                // Only show if the scroller is not at the bottom
                enabled = jumpVisibility,
                scrollUp = true,
                onClicked = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HistoryColumns(
    listState: LazyListState,
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val favourites = viewModel.favourites.observeAsState(emptyList())
    val downloads = viewModel.downloads.observeAsState(emptyList())
    val watchHistory = viewModel.watchHistory.observeAsState(emptyList())

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        stickyHeader {
            if (!favourites.value.isNullOrEmpty()) {
                FavouritesRow(viewModel)
            }
        }
        item {
            if (!favourites.value.isNullOrEmpty()) {
                LazyRow {
                    items(favourites.value) { favourite ->
                        ContentItem(interactionEvents, favourite)
                    }
                }
                ListItemDivider()
            }
        }
        stickyHeader {
            if (!downloads.value.isNullOrEmpty()) {
                DownloadsRow(viewModel)
            }
        }
        items(downloads.value) { download ->
            DownloadItem(download, interactionEvents, viewModel)
        }
        item {
            if (!downloads.value.isNullOrEmpty()) {
                ListItemDivider()
            }
        }
        stickyHeader {
            if (!watchHistory.value.isNullOrEmpty()) {
                HistoryRow(viewModel)
            }
        }
        items(watchHistory.value) { history ->
            HistoryItem(history, interactionEvents, viewModel)
        }
        item {
            if (!watchHistory.value.isNullOrEmpty()) {
                ListItemDivider()
            }
        }
        item {
            if (favourites.value.isNullOrEmpty() && watchHistory.value.isNullOrEmpty()) {
                EmptyHistory()
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

}

@Composable
fun FavouritesRow(viewModel: DashboardViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.favourites),
            style = typography.h6,
            modifier = Modifier.padding(4.dp)
        )
        IconButton(onClick = { viewModel.clearFavourites() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun DownloadsRow(viewModel: DashboardViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.downloads),
            style = typography.h6,
            modifier = Modifier.padding(4.dp)
        )
        IconButton(onClick = { viewModel.clearDownloads() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun HistoryRow(viewModel: DashboardViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.watch_history),
            style = typography.h6,
            modifier = Modifier.padding(4.dp)
        )
        IconButton(onClick = { viewModel.clearHistory() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun DownloadItem(
    download: Download,
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    Card(modifier = Modifier.padding(8.dp), elevation = 2.dp) {
        Row {
            val painter = rememberCoilPainter(
                request = Constants.XMOVIES8_STATIC_URL + download.history.poster,
                fadeIn = true
            )
            Image(
                painter = painter,
                contentDescription = download.history.title,
                modifier = Modifier
                    .width(40.dp)
                    .height(60.dp)
                    .clickable(onClick = {
                        interactionEvents(DashboardInteractionEvents.OpenMovieDetail(download.history.hash))
                    })
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp)
                    .clickable(onClick = {
                        interactionEvents(DashboardInteractionEvents.OpenDownloadedFile(download))
                    })
            ) {
                Text(
                    text = download.history.title,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.h6.copy(fontSize = 14.sp)
                )
                Text(
                    text = download.history.episode,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.subtitle2
                )
            }
            IconButton(
                onClick = { viewModel.removeFromDownload(download) },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun HistoryItem(
    history: History,
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    Card(modifier = Modifier.padding(8.dp), elevation = 2.dp) {
        Row {
            val painter = rememberCoilPainter(
                request = Constants.XMOVIES8_STATIC_URL + history.poster,
                fadeIn = true
            )
            Image(
                painter = painter,
                contentDescription = history.title,
                modifier = Modifier
                    .width(50.dp)
                    .height(80.dp)
                    .clickable(onClick = {
                        interactionEvents(DashboardInteractionEvents.OpenMovieDetail(history.hash))
                    })
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp)
                    .clickable(onClick = {
                        interactionEvents(DashboardInteractionEvents.ResumePlayback(history))
                    })
            ) {
                Text(
                    text = history.title,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.h6.copy(fontSize = 14.sp)
                )
                Text(
                    text = history.episode,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.subtitle2
                )
                if (history.position != 0L && history.duration != 0L) {
                    val progress: Float = (history.position.toFloat() / history.duration.toFloat())
                    val animatedProgress = animateFloatAsState(
                        targetValue = progress,
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                    ).value
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            IconButton(
                onClick = { viewModel.removeFromHistory(history) },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun EmptyHistory() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.watch_history),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.watch_history_info),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}