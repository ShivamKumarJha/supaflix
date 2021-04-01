package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import com.shivamkumarjha.supaflix.ui.theme.GraySurface

@Composable
fun BottomNavigation() {
    val items = listOf(
        DashboardNavigation.Home,
        DashboardNavigation.Search,
        DashboardNavigation.History,
        DashboardNavigation.Settings,
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    val bottomNavBackground =
        if (isSystemInDarkTheme()) GraySurface else MaterialTheme.colors.background

    Scaffold(bottomBar = {
        BottomNavigation(backgroundColor = bottomNavBackground) {
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.drawableId),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(screen.stringId)) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo = navController.graph.startDestination
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
    }) {
        NavHost(navController, startDestination = DashboardNavigation.Home.route) {
            composable(DashboardNavigation.Home.route) { HomeContent(navController) }
            composable(DashboardNavigation.Search.route) { HomeContent(navController) }
            composable(DashboardNavigation.History.route) { HomeContent(navController) }
            composable(DashboardNavigation.Settings.route) { HomeContent(navController) }
        }
    }
}