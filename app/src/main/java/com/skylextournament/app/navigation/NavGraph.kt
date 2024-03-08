package com.skylextournament.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.skylextournament.app.home.ui.HomeScreen
import com.skylextournament.app.login.ui.LoginScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screens.Login.route,
        route = AUTH_GRAPH_ROUTE
    ) {
        composable(
            route = Screens.Login.route
        ) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screens.Home.route)
                }
            )
        }
        composable(
            route = Screens.Home.route,
        ) {
            HomeScreen()
        }
    }
}
