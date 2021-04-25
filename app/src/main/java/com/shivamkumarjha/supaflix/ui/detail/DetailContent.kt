package com.shivamkumarjha.supaflix.ui.detail

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.ui.theme.ColorUtility
import com.shivamkumarjha.supaflix.utility.Utility

@Composable
fun DetailScreen(hash: String, interactionEvents: (DetailInteractionEvents) -> Unit) {
    val viewModel: DetailViewModel = viewModel()
    val content = viewModel.content.observeAsState(Resource.loading(null))
    viewModel.checkFavourite(hash)
    viewModel.watch(hash)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = content.value?.data?.name ?: "",
                        style = MaterialTheme.typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        interactionEvents(DetailInteractionEvents.NavigateUp(true))
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = {
            if (content.value.data != null) {
                DetailContent(content.value.data!!, viewModel)
            } else {
                ShowProgressBar()
            }

        },
        bottomBar = {
            if (content.value.data != null) {
                BottomBar(content.value.data!!, viewModel)
            }
        }
    )
}

@Composable
fun DetailContent(content: Content, viewModel: DetailViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorUtility.surfaceBackground(isSystemInDarkTheme()))
    ) {
        item {

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
        modifier = Modifier
            .fillMaxSize()
            .background(ColorUtility.surfaceBackground(isSystemInDarkTheme())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}