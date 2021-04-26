package com.shivamkumarjha.supaflix.ui.search

import android.animation.ValueAnimator
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.airbnb.lottie.LottieAnimationView
import com.google.accompanist.coil.rememberCoilPainter
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Contents
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.ui.theme.ThemeUtility

@Composable
fun SearchMovieContent(
    searchType: SearchType,
    property: Property?,
    keyWord: String?,
    interactionEvents: (SearchInteractionEvents) -> Unit
) {
    val viewModel: SearchViewModel = viewModel()
    val listScrollState = rememberLazyListState()
    val searchResult: LazyPagingItems<Contents> = when (searchType) {
        SearchType.SEARCH_ACTOR -> viewModel.getActor(property!!).collectAsLazyPagingItems()
        SearchType.SEARCH_COUNTRY -> viewModel.getCountry(property!!).collectAsLazyPagingItems()
        SearchType.SEARCH_DIRECTOR -> viewModel.getDirector(property!!).collectAsLazyPagingItems()
        SearchType.SEARCH_GENRE -> viewModel.getGenre(property!!).collectAsLazyPagingItems()
        SearchType.SEARCH_MOVIE -> viewModel.getMovie(keyWord!!).collectAsLazyPagingItems()
        SearchType.SEARCH_YEAR -> viewModel.getRelease(keyWord!!).collectAsLazyPagingItems()
    }
    val title = when (searchType) {
        SearchType.SEARCH_ACTOR -> property!!.name
        SearchType.SEARCH_COUNTRY -> property!!.name
        SearchType.SEARCH_DIRECTOR -> property!!.name
        SearchType.SEARCH_GENRE -> property!!.name
        SearchType.SEARCH_MOVIE -> keyWord!!
        SearchType.SEARCH_YEAR -> keyWord!!
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        interactionEvents(SearchInteractionEvents.NavigateUp)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeUtility.surfaceBackground(isSystemInDarkTheme()))
        ) {
            SearchPagingItems(searchResult, listScrollState, interactionEvents)
        }
    }
}

@Composable
fun SearchPagingItems(
    pagingItems: LazyPagingItems<Contents>,
    listScrollState: LazyListState,
    interactionEvents: (SearchInteractionEvents) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(state = listScrollState) {
        items(pagingItems) { contents ->
            contents?.let {
                SearchItem(contents, interactionEvents)
            }
        }

        pagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> item {
                    LottieLoadingView(context = context)
                }
                loadState.append is LoadState.Loading -> {
                    item { LottieLoadingView(context = context) }
                }
            }
        }
    }
}

@Composable
fun SearchItem(content: Contents, interactionEvents: (SearchInteractionEvents) -> Unit) {
    val typography = MaterialTheme.typography
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val painter = rememberCoilPainter(
            request = Constants.XMOVIES8_STATIC_URL + content.poster_path,
            fadeIn = true
        )
        Image(
            painter = painter,
            contentDescription = content.name,
            modifier = Modifier
                .width(150.dp)
                .height(225.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = {
                    interactionEvents(SearchInteractionEvents.OpenDetail(content))
                }),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = content.name,
                color = ThemeUtility.textColor(isSystemInDarkTheme()),
                style = typography.h6
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = content.released,
                color = ThemeUtility.textColor(isSystemInDarkTheme()),
                style = typography.body2
            )
        }
    }
}

@Composable
fun LottieLoadingView(context: Context) {
    val lottieView = remember {
        LottieAnimationView(context).apply {
            setAnimation("loading.json")
            repeatCount = ValueAnimator.INFINITE
        }
    }
    AndroidView(
        { lottieView }, modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        it.playAnimation()
    }
}