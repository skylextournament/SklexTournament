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
            R.string.top_app_bar_title_tournaments,
            Icons.Default.VideogameAsset,
            Screens.Tournaments.route
        ),
        BottomNavigationItem(R.string.top_app_bar_title_team, Icons.Default.Group, Screens.Team.route),
        BottomNavigationItem(R.string.top_app_bar_title_profile, Icons.Default.Person, Screens.Profile.route),
    )
}