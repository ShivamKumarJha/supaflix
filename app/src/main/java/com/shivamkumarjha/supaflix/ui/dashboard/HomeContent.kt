package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.xmovies.Contents
import com.shivamkumarjha.supaflix.model.xmovies.Covers
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.ui.theme.ThemeUtility

@Composable
fun HomeContent(
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    viewModel: DashboardViewModel
) {
    viewModel.initialize()
    val home = viewModel.home.observeAsState(Resource.loading(null))
    val trending = viewModel.trending.observeAsState(Resource.loading(null))
    val featured = viewModel.featured.observeAsState(Resource.loading(null))
    val recentMovies = viewModel.recentMovies.observeAsState(Resource.loading(null))
    val mostViewedMovies = viewModel.mostViewedMovies.observeAsState(Resource.loading(null))
    val topIMBDMovies = viewModel.topIMBDMovies.observeAsState(Resource.loading(null))
    val recentSeries = viewModel.recentSeries.observeAsState(Resource.loading(null))
    val mostViewedSeries = viewModel.mostViewedSeries.observeAsState(Resource.loading(null))
    val topIMBDSeries = viewModel.topIMBDSeries.observeAsState(Resource.loading(null))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeUtility.surfaceBackground(isSystemInDarkTheme()))
    ) {
        item {
            if (trending.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.trending),
                    contents = trending.value.data?.contents
                )
            }
        }
        item {
            if (featured.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.featured),
                    contents = featured.value.data?.contents
                )
            }
        }
        item {
            if (home.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.movies),
                    contents = home.value.data?.movies
                )
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.series),
                    contents = home.value.data?.series
                )
                CoversRow(interactionEvents, home.value.data?.covers)
            }
        }
        item {
            if (recentMovies.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.recent_movies),
                    contents = recentMovies.value.data?.contents
                )
            }
        }
        item {
            if (mostViewedMovies.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.most_viewed_movies),
                    contents = mostViewedMovies.value.data?.contents
                )
            }
        }
        item {
            if (topIMBDMovies.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.top_imdb_movies),
                    contents = topIMBDMovies.value.data?.contents
                )
            }
        }
        item {
            if (recentSeries.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.recent_series),
                    contents = recentSeries.value.data?.contents
                )
            }
        }
        item {
            if (mostViewedSeries.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.most_viewed_series),
                    contents = mostViewedSeries.value.data?.contents
                )
            }
        }
        item {
            if (topIMBDSeries.value.data != null) {
                ContentsRow(
                    interactionEvents = interactionEvents,
                    heading = stringResource(id = R.string.top_imdb_series),
                    contents = topIMBDSeries.value.data?.contents
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ContentsRow(
    interactionEvents: (DashboardInteractionEvents) -> Unit,
    heading: String,
    contents: List<Contents>?
) {
    if (contents.isNullOrEmpty())
        return
    Text(
        text = heading,
        style = typography.h6,
        color = ThemeUtility.textColor(isSystemInDarkTheme()),
        modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
    )
    LazyRow {
        items(contents) { content ->
            val favourite = Favourite(
                content.hash,
                content.name,
                content.poster_path,
                content.released,
                null,
                null
            )
            ContentItem(interactionEvents, favourite)
        }
    }
    ListItemDivider()
}

@Composable
fun ContentItem(interactionEvents: (DashboardInteractionEvents) -> Unit, favourite: Favourite) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .height(225.dp)
            .requiredWidth(180.dp)
            .padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
            .clickable(
                onClick = {
                    interactionEvents(
                        DashboardInteractionEvents.OpenMovieDetail(
                            favourite.hash
                        )
                    )
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberCoilPainter(
                request = Constants.XMOVIES8_STATIC_URL + favourite.poster,
                fadeIn = true
            )
            Image(
                painter = painter,
                contentDescription = favourite.title,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            when (painter.loadState) {
                ImageLoadState.Loading -> {
                    // Display a circular progress indicator whilst loading
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                else -> {

                }
            }

            Text(
                text = favourite.title,
                color = Color.White,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f))
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun CoversRow(interactionEvents: (DashboardInteractionEvents) -> Unit, covers: List<Covers>?) {
    if (covers.isNullOrEmpty())
        return
    Text(
        text = stringResource(id = R.string.top_picks),
        style = typography.h6,
        color = ThemeUtility.textColor(isSystemInDarkTheme()),
        modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
    )
    LazyRow {
        items(covers) { cover ->
            CoverItem(interactionEvents, cover)
        }
    }
    ListItemDivider()
}

@Composable
fun CoverItem(interactionEvents: (DashboardInteractionEvents) -> Unit, cover: Covers) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .height(189.dp)
            .requiredWidth(336.dp)
            .padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
            .clickable(
                onClick = {
                    interactionEvents(DashboardInteractionEvents.OpenMovieDetail(cover.contentHash))
                }
            )
    ) {
        Box {
            val painter = rememberCoilPainter(request = cover.coverUrl, fadeIn = true)
            Image(
                painter = painter,
                contentDescription = cover.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            when (painter.loadState) {
                ImageLoadState.Loading -> {
                    // Display a circular progress indicator whilst loading
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                else -> {

                }
            }
        }
    }
}

@Composable
fun ListItemDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}