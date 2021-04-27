package com.shivamkumarjha.supaflix.ui.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.ui.theme.gradientBluePurple
import com.shivamkumarjha.supaflix.ui.theme.horizontalGradientBackground

enum class ProfileType {
    GITHUB, TWITTER, LINKEDIN
}

private const val initialImageFloat = 170f

private fun launchSocialActivity(context: Context, profileType: ProfileType) {
    val intent = when (profileType) {
        ProfileType.GITHUB -> Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_GITHUB))
        ProfileType.TWITTER -> Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWITTER))
        ProfileType.LINKEDIN -> Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_LINKEDIN))
    }
    context.startActivity(intent)
}

@Composable
fun SettingsScreen(viewModel: DashboardViewModel) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .semantics { testTag = "Profile Screen" }
        ) {
            val scrollState = rememberScrollState(0)
            TopAppBarView(scrollState.value.toFloat())
            TopBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                TopScrollingContent(scrollState)
                BottomScrollingContent()
                Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                ServerSwitch(viewModel)
                SubtitleSwitch(viewModel)
                LandscapePlayerSwitch(viewModel)
                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}

@Composable
fun TopAppBarView(scroll: Float) {
    if (scroll > initialImageFloat + 5) {
        TopAppBar(
            title = {
                Text(text = Constants.DEV_NAME)
            },
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.dev),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        )
    }
}

@Composable
private fun TopBackground() {
    Spacer(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .horizontalGradientBackground(gradientBluePurple)
    )
}

@Composable
fun TopScrollingContent(scrollState: ScrollState) {
    val visibilityChangeFloat = scrollState.value > initialImageFloat - 20
    Row {
        AnimatedImage(scroll = scrollState.value.toFloat())
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 48.dp)
                .alpha(animateFloatAsState(if (visibilityChangeFloat) 0f else 1f).value)
        ) {
            Text(
                text = Constants.DEV_NAME,
                style = typography.h6.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(id = R.string.about_me_role),
                style = typography.subtitle2
            )
        }
    }
}

@Composable
fun AnimatedImage(scroll: Float) {
    val dynamicAnimationSizeValue = (initialImageFloat - scroll).coerceIn(36f, initialImageFloat)
    Image(
        painter = painterResource(id = R.drawable.dev),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 16.dp)
            .size(animateDpAsState(Dp(dynamicAnimationSizeValue)).value)
            .clip(CircleShape)
    )
}

@Composable
fun BottomScrollingContent() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(8.dp)
    ) {
        SocialRow()
        Text(
            text = stringResource(id = R.string.about_me),
            style = typography.h6,
            modifier = Modifier.padding(start = 8.dp, top = 12.dp)
        )
        Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
        Text(
            text = stringResource(id = R.string.about_me_info),
            style = typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
        Text(
            text = stringResource(id = R.string.about_project),
            style = typography.h6,
            modifier = Modifier.padding(start = 8.dp, top = 16.dp)
        )
        Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
        Text(
            text = stringResource(id = R.string.about_project_info),
            style = typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

@Composable
fun SocialRow() {
    Card(elevation = 8.dp, modifier = Modifier.padding(8.dp)) {
        val context = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            IconButton(onClick = { launchSocialActivity(context, ProfileType.GITHUB) }) {
                Icon(painterResource(id = R.drawable.ic_github), contentDescription = null)
            }
            IconButton(onClick = { launchSocialActivity(context, ProfileType.TWITTER) }) {
                Icon(painterResource(id = R.drawable.ic_twitter), contentDescription = null)
            }
            IconButton(onClick = { launchSocialActivity(context, ProfileType.LINKEDIN) }) {
                Icon(painterResource(id = R.drawable.ic_linkedin), contentDescription = null)
            }
        }
    }
}

@Composable
fun ServerSwitch(viewModel: DashboardViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.automatically_pick_best_server),
            style = typography.h6.copy(fontSize = 14.sp),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(8.dp)
        )
        val checkedState = remember { mutableStateOf(viewModel.getServerSwitch()) }
        Switch(
            checked = checkedState.value,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(8.dp),
            onCheckedChange = {
                checkedState.value = it
                viewModel.setServerSwitch(it)
            }
        )
    }
}

@Composable
fun SubtitleSwitch(viewModel: DashboardViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.show_subtitles_when_available),
            style = typography.h6.copy(fontSize = 14.sp),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(8.dp)
        )
        val checkedState = remember { mutableStateOf(viewModel.getSubtitleSwitch()) }
        Switch(
            checked = checkedState.value,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(8.dp),
            onCheckedChange = {
                checkedState.value = it
                viewModel.setSubtitleSwitch(it)
            }
        )
    }
}

@Composable
fun LandscapePlayerSwitch(viewModel: DashboardViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.landscape_player),
            style = typography.h6.copy(fontSize = 14.sp),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(8.dp)
        )
        val checkedState = remember { mutableStateOf(viewModel.getLandscapePlayer()) }
        Switch(
            checked = checkedState.value,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(8.dp),
            onCheckedChange = {
                checkedState.value = it
                viewModel.setLandscapePlayer(it)
            }
        )
    }
}