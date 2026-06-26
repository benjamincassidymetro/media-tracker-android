package edu.metrostate.ics342.mediatracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.metrostate.ics342.mediatracker.R

sealed class NavItem(val route: String, val icon: ImageVector, val labelRes: Int) {
    object Feed : NavItem(Routes.ACTIVITY_FEED, Icons.Default.Home, R.string.nav_feed)
    object Search : NavItem(Routes.SEARCH, Icons.Default.Search, R.string.nav_search)
    object Library : NavItem(Routes.LIBRARY, Icons.Default.CollectionsBookmark, R.string.nav_library)
    object Connections : NavItem(Routes.CONNECTIONS, Icons.Default.People, R.string.nav_people)
    object Profile : NavItem(Routes.MY_PROFILE, Icons.Default.Person, R.string.nav_profile)
}

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem.Feed,
        NavItem.Search,
        NavItem.Library,
        NavItem.Connections,
        NavItem.Profile
    )

    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(item.labelRes)) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
