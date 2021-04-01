package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Contents
import com.shivamkumarjha.supaflix.model.xmovies.Covers
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.ui.theme.ColorUtility
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeContent(navController: NavController, viewModel: DashboardViewModel) {
    viewModel.initialize()
    val home = viewModel.home.observeAsState(Resource.loading(null))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorUtility.surfaceBackground(isSystemInDarkTheme()))
    ) {
        if (home.value.data != null) {
            item {
                ContentsRow(
                    heading = stringResource(id = R.string.movies),
                    contents = home.value.data?.movies ?: listOf()
                )
                ContentsRow(
                    heading = stringResource(id = R.string.series),
                    contents = home.value.data?.series ?: listOf()
                )
                CoversRow(covers = home.value.data?.covers ?: listOf())
            }
        }
    }
}

@Composable
fun ContentsRow(heading: String, contents: List<Contents>) {
    val modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
    Text(
        text = heading,
        style = typography.h6,
        color = ColorUtility.textColor(isSystemInDarkTheme()),
        modifier = modifier
    )
    LazyRow {
        items(contents) { content ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                CoilImage(
                    data = Constants.XMOVIES8_STATIC_URL + content.poster_path,
                    contentDescription = content.name,
                    loading = {
                        Box(Modifier.matchParentSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    },
                    modifier = Modifier
                        .height(225.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "\t" + content.name + "\t",
                    color = ColorUtility.textColor(isSystemInDarkTheme()),
                    style = typography.body2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    ListItemDivider()
}

@Composable
fun CoversRow(covers: List<Covers>) {
    Text(
        text = stringResource(id = R.string.top_picks),
        style = typography.h6,
        color = ColorUtility.textColor(isSystemInDarkTheme()),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
    )
    LazyRow {
        items(covers) { cover ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CoilImage(
                    data = cover.coverUrl,
                    contentDescription = cover.name,
                    loading = {
                        Box(Modifier.matchParentSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    },
                    modifier = Modifier
                        .height(225.dp)
                        .fillParentMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit,
                    fadeIn = true
                )
                Text(
                    text = "\t" + cover.name + "\t",
                    color = ColorUtility.textColor(isSystemInDarkTheme()),
                    style = typography.body2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    ListItemDivider()
}

@Composable
fun ListItemDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}