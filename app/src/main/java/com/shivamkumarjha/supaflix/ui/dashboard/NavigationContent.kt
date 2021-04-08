package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.google.accompanist.insets.navigationBarsPadding
import com.shivamkumarjha.supaflix.ui.theme.ColorUtility
import com.shivamkumarjha.supaflix.ui.theme.Green500

@Composable
fun BottomNavigation(dashboardInteractionEvents: (DashboardInteractionEvents) -> Unit) {
    val viewModel: DashboardViewModel = viewModel()

    //Navigation
    val items = listOf(
        DashboardNavigation.Home,
        DashboardNavigation.Search,
        DashboardNavigation.History,
        DashboardNavigation.Settings,
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    Scaffold(bottomBar = {
        BottomNavigation(backgroundColor = ColorUtility.surfaceBackground(isSystemInDarkTheme())) {
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
                    alwaysShowLabel = false,
                    selectedContentColor = Green500,
                    unselectedContentColor = LocalContentColor.current,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }) {
        NavHost(navController, startDestination = DashboardNavigation.Home.route) {
            composable(DashboardNavigation.Home.route) { HomeContent(navController, viewModel) }
            composable(DashboardNavigation.Search.route) { SearchScreen(navController) }
            composable(DashboardNavigation.History.route) { SearchScreen(navController) }
            composable(DashboardNavigation.Settings.route) { SearchScreen(navController) }
        }
    }
}