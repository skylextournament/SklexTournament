package com.skylextournament.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.skylextournament.app.home.ui.HomeScreen
import com.skylextournament.app.login.ui.LoginScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(navController = navController)
    }
}

@ExperimentalMaterial3Api
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
