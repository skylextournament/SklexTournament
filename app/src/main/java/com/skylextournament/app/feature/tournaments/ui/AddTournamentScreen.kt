package com.skylextournament.app.feature.tournaments.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skylextournament.app.R
import com.skylextournament.app.feature.tournaments.data.TournamentsState
import com.skylextournament.app.ui.common.DateTimePickerComponent
import com.skylextournament.app.ui.common.NormalTextField
import com.skylextournament.app.ui.common.SkylexButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTournamentScreen(state: TournamentsState.AddTournament, viewModel: TournamentsViewModel) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_create_tournament)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.load() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate Back")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                SkylexButton(
                    text = stringResource(id = R.string.title_create_tournament),
                    onClick = {
                        viewModel.createTournament()
                    },
                )
            }
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            NormalTextField(
                value = state.tournament.name,
                label = stringResource(id = R.string.textfield_tournament_name),
                singleLine = false,
                onValueChange = { newValue ->
                    viewModel.onTournamentNameChange(newValue)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.VideogameAsset,
                        contentDescription = null
                    )
                },
                onDone = { }
            )
            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(
                value = state.tournament.overview,
                label = stringResource(id = R.string.textfield_tournament_overview),
                onValueChange = { newValue ->
                    viewModel.onTournamentOverviewChange(newValue)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null
                    )
                },
                singleLine = false,
                modifier = Modifier
                    .height(160.dp)
                    .verticalScroll(rememberScrollState()),
                onDone = { }
            )

            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(
                value = state.tournament.maxTeamsCount.toString(),
                label = stringResource(id = R.string.textfield_tournament_max_team_count),
                onValueChange = { newValue ->
                    viewModel.onTournamentMaxTeamCountChange(newValue)
                },
                keyboardType = KeyboardType.Number,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = null
                    )
                },
                onDone = { }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.select_start_date))
            DateTimePickerComponent(
                saveSelectedDate = viewModel::onTournamentStartDateSelected,
                showSelectDateAfterToday = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Selected date should be after today, please select again")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.select_registration_deadline))
            DateTimePickerComponent(
                saveSelectedDate = viewModel::onTournamentRegistrationDeadlineSelected,
                showSelectDateAfterToday = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Selected date should be after today, please select again")
                    }
                }
            )
        }
    }
}