package com.skylextournament.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.skylextournament.app.feature.home.HomeScreen
import com.skylextournament.app.feature.login.ui.LoginScreen
import com.skylextournament.app.feature.registration.ui.RegistrationScreen

@Composable
fun NavGraph(
    isLoggedIn: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(
            isLoggedIn = isLoggedIn,
            navController = navController,
        )
    }
}

fun NavGraphBuilder.authNavGraph(
    isLoggedIn: Boolean,
    navController: NavHostController
) {
    navigation(
        startDestination = if (isLoggedIn) Screens.HomeScreen.route else Screens.LoginScreen.route,
        route = AUTH_GRAPH_ROUTE
    ) {
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Screens.HomeScreen.route)
                },
                onRegisterClick = {
                    navController.navigate(Screens.RegistrationScreen.route)
                }
            )
        }
        composable(
            route = Screens.RegistrationScreen.route,
        ) {
            RegistrationScreen(
                navigateToHome = {
                    navController.navigate(Screens.HomeScreen.route)
                }
            )
        }
        composable(
            route = Screens.HomeScreen.route,
        ) {
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(Screens.LoginScreen.route)
                }
            )
        }
    }
}
