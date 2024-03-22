package com.skylextournament.app.navigation

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "login")
    object RegistrationScreen : Screens(route = "registration")
    object HomeScreen : Screens(route = "home")
    object TournamentsScreen : Screens(route = "tournaments")
    object TeamScreen : Screens(route = "team")
    object ProfileScreen : Screens(route = "profile") {
        object Args {
            const val username = "username"
        }
    }
}