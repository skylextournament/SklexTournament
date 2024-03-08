package com.skylextournament.app.navigation

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"

sealed class Screens(val route: String) {
    object Login : Screens(route = "login")
    object Home : Screens(route = "home") {
        fun passUsername() = ""

        object Args {
            const val username = "username"
        }
    }

    object Tournaments : Screens(route = "tournaments") {
        object Args {
            const val username = "username"
        }
    }

    object Team : Screens(route = "team") {
        object Args {
            const val username = "username"
        }
    }

    object Profile : Screens(route = "profile") {
        object Args {
            const val username = "username"
        }
    }
}