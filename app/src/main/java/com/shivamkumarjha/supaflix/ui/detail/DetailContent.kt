package com.shivamkumarjha.supaflix.ui.detail

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.statusBarsPadding
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.model.xmovies.Episode
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.model.xmovies.SimilarContents
import com.shivamkumarjha.supaflix.ui.theme.Green200
import com.shivamkumarjha.supaflix.ui.theme.Green500
import com.shivamkumarjha.supaflix.ui.theme.Green700
import com.shivamkumarjha.supaflix.utility.Utility
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DetailScreen(hash: String, interactionEvents: (DetailInteractionEvents) -> Unit) {
    val viewModel: DetailViewModel = viewModel()
    viewModel.checkFavourite(hash)
    viewModel.content(hash)
    val content = viewModel.watch(hash).observeAsState(null)

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        bottomBar = {
            if (content.value != null) {
                BottomBar(content.value!!, viewModel)
            }
        }
    ) {
        if (content.value != null) {
            DetailBackdropScaffold(content.value!!, viewModel, interactionEvents)
        } else {
            ShowProgressBar()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun DetailBackdropScaffold(
    content: Content,
    viewModel: DetailViewModel,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    val expand = remember { mutableStateOf(false) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    val scope = rememberCoroutineScope()

    BackdropScaffold(
        modifier = Modifier.background(if (isSystemInDarkTheme()) Color.Black else Color.White),
        scaffoldState = scaffoldState,
        frontLayerScrimColor = Color.Transparent,
        appBar = {
            TopAppBar(
                title = {
                    Text(
                        text = content.name,
                        style = typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        interactionEvents(DetailInteractionEvents.NavigateUp)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        backLayerContent = {
            Column(
                Modifier.padding(
                    animateDpAsState(
                        if (expand.value) 1.dp else 120.dp,
                        tween(350)
                    ).value
                )
            ) {
                val link = if (!content.backdrop_url.isNullOrEmpty())
                    content.backdrop_url
                else
                    Constants.XMOVIES8_STATIC_URL + content.poster_path
                val painter = rememberCoilPainter(request = link)
                Image(
                    painter = painter,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                scaffoldState.conceal()
                            }
                        }
                )
                when (painter.loadState) {
                    is ImageLoadState.Success -> expand.value = true
                    else -> expand.value = false
                }
            }

        },
        frontLayerContent = {
            DetailContent(content, viewModel, interactionEvents)
        }
    )
}

@Composable
fun DetailContent(
    content: Content,
    viewModel: DetailViewModel,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = content.name,
                modifier = Modifier.padding(8.dp),
                style = typography.h6
            )
        }
        item {
            PropertySection(content.genres, PropertyType.GENRE, interactionEvents)
        }
        item {
            PropertySection(content.actors, PropertyType.ACTORS, interactionEvents)
        }
        item {
            PropertySection(content.directors, PropertyType.DIRECTORS, interactionEvents)
        }
        item {
            PropertySection(content.countries, PropertyType.COUNTRY, interactionEvents)
        }
        item {
            if (content.released != null)
                BodyText("${stringResource(id = R.string.release)}: ${content.released}")
        }
        item {
            if (content.duration != null)
                BodyText("${stringResource(id = R.string.duration)}: ${content.duration}")
        }
        item {
            if (content.imdb_rating != null)
                BodyText("${stringResource(id = R.string.imbd)}: ${content.imdb_rating}")
        }
        item {
            if (content.description != null)
                DescriptionText(content.description)
        }
        item {
            ShowEpisodes(viewModel, content, interactionEvents)
        }
        item {
            SimilarContents(content.similarContents, interactionEvents)
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

enum class PropertyType {
    GENRE, ACTORS, DIRECTORS, COUNTRY
}

@Composable
fun PropertySection(
    property: List<Property>,
    type: PropertyType,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    LazyRow {
        items(property) {
            PropertyView(it, type, interactionEvents)
        }
    }
}

@Composable
fun PropertyView(
    property: Property,
    type: PropertyType,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    Text(
        text = property.name,
        color = Green700,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Green200.copy(alpha = 0.2f))
            .clickable(onClick = {
                when (type) {
                    PropertyType.GENRE -> interactionEvents(
                        DetailInteractionEvents.SearchGenre(property)
                    )
                    PropertyType.ACTORS -> interactionEvents(
                        DetailInteractionEvents.SearchActor(property)
                    )
                    PropertyType.DIRECTORS -> interactionEvents(
                        DetailInteractionEvents.SearchDirector(property)
                    )
                    PropertyType.COUNTRY -> interactionEvents(
                        DetailInteractionEvents.SearchCountry(property)
                    )
                }
            })
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = typography.body2.copy(fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Center
    )
}

@Composable
fun BodyText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = typography.subtitle2
    )
}

@Composable
fun DescriptionText(description: String) {
    var seeMore by remember { mutableStateOf(true) }
    Text(
        text = description,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = typography.subtitle2,
        maxLines = if (seeMore) 2 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
    )
    val textButton = if (seeMore) {
        stringResource(id = R.string.see_more)
    } else {
        stringResource(id = R.string.see_less)
    }
    Text(
        text = textButton,
        style = typography.button,
        textAlign = TextAlign.Center,
        color = Green500,
        modifier = Modifier
            .heightIn(20.dp)
            .fillMaxWidth()
            .padding(top = 15.dp)
            .clickable {
                seeMore = !seeMore
            }
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
fun ShowEpisodes(
    viewModel: DetailViewModel,
    content: Content,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    Text(
        text = stringResource(id = R.string.watch_now),
        style = typography.h6,
        modifier = Modifier.padding(8.dp)
    )
    LazyRow {
        items(content.episodes) { episode ->
            EpisodeButton(viewModel, episode, content, interactionEvents)
        }
    }
}

@Composable
fun EpisodeButton(
    viewModel: DetailViewModel,
    episode: Episode,
    content: Content,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    Button(
        onClick = {
            val history = viewModel.getHistory(episode, content)
            interactionEvents(DetailInteractionEvents.OpenEpisode(history))
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = episode.name + "\t",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun SimilarContents(
    similarContents: List<SimilarContents>,
    interactionEvents: (DetailInteractionEvents) -> Unit
) {
    Text(
        text = stringResource(id = R.string.similar_contents),
        style = typography.h6,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 16.dp)
    )
    LazyRow {
        items(similarContents) { content ->
            Card(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(225.dp)
                    .requiredWidth(180.dp)
                    .padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            interactionEvents(
                                DetailInteractionEvents.OpenMovieDetail(content)
                            )
                        }
                    )) {
                    val painter = rememberCoilPainter(
                        request = Constants.XMOVIES8_STATIC_URL + content.poster_path,
                        fadeIn = true
                    )
                    Image(
                        painter = painter,
                        contentDescription = content.name,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (painter.loadState is ImageLoadState.Loading) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }

                    Text(
                        text = content.name,
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
    }
}

@Composable
fun BottomBar(content: Content, viewModel: DetailViewModel) {
    Surface(elevation = 2.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            FavouriteButton(viewModel = viewModel, content = content)
            val context = LocalContext.current
            IconButton(onClick = { sharePost(content, context) }) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = null)
            }
        }
    }
}

@Composable
fun FavouriteButton(viewModel: DetailViewModel, content: Content) {
    val isFavourite = viewModel.isFavourite.observeAsState(initial = false)
    val clickLabel = stringResource(
        if (isFavourite.value) R.string.favourite else R.string.remove_favourite
    )
    val context = LocalContext.current
    IconToggleButton(
        checked = isFavourite.value,
        onCheckedChange = {
            viewModel.toggleFavourites(content, it)
            val message = if (it)
                content.name + " " + context.getString(R.string.favourite_add)
            else
                content.name + " " + context.getString(R.string.favourite_remove)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.semantics {
            // Use a custom click label that accessibility services can communicate to the user.
            // We only want to override the label, not the actual action, so for the action we pass null.
            this.onClick(label = clickLabel, action = null)
        }
    ) {
        Icon(
            imageVector = if (isFavourite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null // handled by click label of parent
        )
    }
}

private fun sharePost(content: Content, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, content.name)
        putExtra(Intent.EXTRA_TEXT, Utility.getWebLink(content))
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_content)))
}

@Composable
fun ShowProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}