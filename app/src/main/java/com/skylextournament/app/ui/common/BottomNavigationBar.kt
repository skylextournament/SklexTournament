package com.skylextournament.app.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.skylextournament.app.feature.profile.ui.ProfileScreen
import com.skylextournament.app.feature.team.ui.TeamScreen
import com.skylextournament.app.feature.tournaments.ui.TournamentsScreen
import com.skylextournament.app.navigation.BottomNavigationItem
import com.skylextournament.app.navigation.Screens

@Composable
fun BottomNavigationBar(
    navigateToLogin: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination?.route,
                        label = {
                            Text(stringResource(navigationItem.label))
                        },
                        icon = {
                            Icon(
                                imageVector = navigationItem.icon,
                                contentDescription = stringResource(navigationItem.label),
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.TournamentsScreen.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(route = Screens.TournamentsScreen.route) {
                TournamentsScreen()
            }
            composable(route = Screens.TeamScreen.route) {
                TeamScreen()
            }
            composable(route = Screens.ProfileScreen.route) {
                ProfileScreen(navigateToLogin)
            }
        }
    }
}