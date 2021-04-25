package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.ui.theme.ThemeUtility

@Composable
fun HistoryContent(
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    val favourites = viewModel.favourites.observeAsState(emptyList())
    val watchHistory = viewModel.watchHistory.observeAsState(emptyList())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeUtility.surfaceBackground(isSystemInDarkTheme()))
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            if (!favourites.value.isNullOrEmpty()) {
                FavouritesRow(favourites.value, interactionEvents, viewModel)
                ListItemDivider()
            }
        }
        item {
            if (!watchHistory.value.isNullOrEmpty()) {
                HistoryRow(watchHistory.value, interactionEvents, viewModel)
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
fun FavouritesRow(
    favourites: List<Favourite>,
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.favourites),
            style = typography.h6,
            color = ThemeUtility.textColor(isSystemInDarkTheme()),
            modifier = Modifier.padding(4.dp)
        )
        IconButton(onClick = { viewModel.clearFavourites() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
    LazyRow {
        items(favourites) { favourite ->
            ContentItem(interactionEvents, favourite)
        }
    }
}

@Composable
fun HistoryRow(
    watchHistory: List<History>,
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.watch_history),
            style = typography.h6,
            color = ThemeUtility.textColor(isSystemInDarkTheme()),
            modifier = Modifier.padding(4.dp)
        )
        IconButton(onClick = { viewModel.clearHistory() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
    watchHistory.forEach { history ->
        HistoryItem(history, interactionEvents, viewModel)
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
                    .width(40.dp)
                    .height(60.dp)
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
            }
            IconButton(
                onClick = { viewModel.removeFromHistory(history) },
                modifier = Modifier.padding(vertical = 4.dp)
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