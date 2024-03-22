package com.skylextournament.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector
import com.skylextournament.app.R

data class BottomNavigationItem(
    @StringRes val label: Int = R.string.top_app_bar_title_tournaments,
    val icon: ImageVector = Icons.Default.Home,
    val route: String = "",
) {

    fun bottomNavigationItems() = listOf(
        BottomNavigationItem(
            label = R.string.top_app_bar_title_tournaments,
            icon = Icons.Default.VideogameAsset,
            route = Screens.TournamentsScreen.route
        ),
        BottomNavigationItem(
            label = R.string.top_app_bar_title_team,
            icon = Icons.Default.Group,
            route = Screens.TeamScreen.route
        ),
        BottomNavigationItem(
            label = R.string.top_app_bar_title_profile,
            icon = Icons.Default.Person,
            route = Screens.ProfileScreen.route
        ),
    )
}