package com.skylextournament.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.skylextournament.app.navigation.NavGraph
import com.skylextournament.app.ui.theme.SkylexTournamentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkylexTournamentTheme {
                NavGraph()
            }
        }
    }
}