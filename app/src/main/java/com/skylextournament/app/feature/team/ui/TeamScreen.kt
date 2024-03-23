package com.skylextournament.app.feature.team.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.ui.common.SkylexError
import com.skylextournament.app.ui.common.SkylexLoader

@Composable
fun TeamScreen(
    viewModel: TeamViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            TeamState.Loading -> SkylexLoader()
            is TeamState.Error -> SkylexError(errorText = state.error) { viewModel.load() }

            is TeamState.HasTeam -> HasTeamScreen(state = state, viewModel = viewModel)

            is TeamState.AddTeam -> AddTeamScreen(state = state, viewModel = viewModel)

            is TeamState.NoTeam -> NoTeamScreen(state = state, viewModel = viewModel)
        }
    }
}