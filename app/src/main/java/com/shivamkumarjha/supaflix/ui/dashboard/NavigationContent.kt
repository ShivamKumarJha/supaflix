package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.shivamkumarjha.supaflix.ui.theme.GraySurface
import com.shivamkumarjha.supaflix.ui.theme.Green500

@Composable
fun BottomNavigation(interactionEvents: (DashboardInteractionEvents) -> Unit) {
    val viewModel: DashboardViewModel = viewModel()

    //Navigation
    val items = listOf(
        DashboardNavigation.Home,
        DashboardNavigation.Search,
        DashboardNavigation.History,
        DashboardNavigation.Settings,
    )
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = if (isSystemInDarkTheme()) GraySurface else Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = screen.imageVector,
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
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        alwaysShowLabel = false,
                        selectedContentColor = Green500,
                        unselectedContentColor = LocalContentColor.current,
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        }
    ) {

        NavHost(navController, startDestination = DashboardNavigation.Home.route) {
            composable(DashboardNavigation.Home.route) {
                HomeContent(interactionEvents, viewModel)
            }
            composable(DashboardNavigation.Search.route) {
                SearchScreen(interactionEvents)
            }
            composable(DashboardNavigation.History.route) {
                HistoryContent(interactionEvents, viewModel)
            }
            composable(DashboardNavigation.Settings.route) {
                SettingsScreen(viewModel)
            }
        }
    }
}