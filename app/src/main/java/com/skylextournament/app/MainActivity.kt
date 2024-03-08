package com.skylextournament.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import com.skylextournament.app.navigation.NavGraph
import com.skylextournament.app.ui.theme.SkylexTournamentTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkylexTournamentTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}