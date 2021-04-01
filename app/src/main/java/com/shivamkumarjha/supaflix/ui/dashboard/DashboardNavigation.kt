package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shivamkumarjha.supaflix.R

sealed class DashboardNavigation(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int
) {
    object Home : DashboardNavigation("home", R.string.home, R.drawable.ic_home)
    object Search : DashboardNavigation("search", R.string.search, R.drawable.ic_search)
    object History : DashboardNavigation("history", R.string.history, R.drawable.ic_history)
    object Settings : DashboardNavigation("settings", R.string.settings, R.drawable.ic_settings)
}