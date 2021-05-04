package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.shivamkumarjha.supaflix.R

sealed class DashboardNavigation(
    val route: String,
    @StringRes val stringId: Int,
    val imageVector: ImageVector
) {
    object Home : DashboardNavigation("home", R.string.home, Icons.Filled.Home)
    object Search : DashboardNavigation("search", R.string.search, Icons.Filled.Search)
    object History : DashboardNavigation("history", R.string.history, Icons.Filled.History)
    object Settings : DashboardNavigation("settings", R.string.settings, Icons.Filled.Settings)
}