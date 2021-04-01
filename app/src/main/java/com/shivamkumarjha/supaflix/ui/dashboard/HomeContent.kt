package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shivamkumarjha.supaflix.model.xmovies.Covers
import com.shivamkumarjha.supaflix.ui.theme.ColorUtility
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeContent(navController: NavController, viewModel: DashboardViewModel) {
    viewModel.initialize()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorUtility.surfaceBackground(isSystemInDarkTheme()))
    ) {

        val covers = viewModel.covers.observeAsState(null)
        if (!covers.value.isNullOrEmpty()) {
            CoversRow(covers = covers.value!!)
            ListItemDivider()
        }
    }
}

@Composable
fun CoversRow(covers: List<Covers>) {
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
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "\t" + cover.name + "\t",
                    style = typography.body2,
                    textAlign = TextAlign.Center
                )
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