package com.skylextournament.app.feature.tournaments.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.feature.tournaments.data.TournamentsState
import com.skylextournament.app.ui.common.SkylexError
import com.skylextournament.app.ui.common.SkylexLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentsScreen(
    viewModel: TournamentsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is TournamentsState.Loading -> SkylexLoader()

                is TournamentsState.Error -> SkylexError(errorText = state.error) { viewModel.load() }

                is TournamentsState.Ready -> TournamentsReadyScreen(state = state, viewModel = viewModel)

                is TournamentsState.AddTournament -> AddTournamentScreen(state = state, viewModel = viewModel)
            }
        }
    }
}