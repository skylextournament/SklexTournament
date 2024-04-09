package com.skylextournament.app.feature.tournaments.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skylextournament.app.R
import com.skylextournament.app.feature.tournaments.data.TournamentsState
import com.skylextournament.app.ui.common.SkylexButton

@Composable
fun TournamentsReadyScreen(state: TournamentsState.Ready, viewModel: TournamentsViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (state.isAdmin) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.goToAddTournament() },
                    icon = { Icon(Icons.Filled.Add, stringResource(id = R.string.button_add_tournament)) },
                    text = { Text(text = stringResource(id = R.string.button_add_tournament)) },
                )
            }
        },
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.top_app_bar_title_tournaments),
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = { viewModel.load() },
                    modifier = Modifier.padding(0.dp),
                    content = {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh list")
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            if (state.tournaments.isEmpty()) {
                Text(text = stringResource(id = R.string.text_empty_tournaments_list))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.tournaments, key = { tournament -> tournament.id }) { tournament ->
                        ListItem(
                            headlineContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = tournament.name)
                                }
                            },
                            supportingContent = {
                                Column(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = tournament.overview
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.padding(top = 24.dp),
                                    ) {
                                        Column {
                                            Text(text = stringResource(id = R.string.tournament_start_date))
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarMonth,
                                                    contentDescription = stringResource(id = R.string.tournament_start_date),
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = viewModel.formatDate(tournament.startDate),
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(36.dp))
                                        Column {
                                            Text(text = stringResource(id = R.string.tournament_registration_deadline))
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarMonth,
                                                    contentDescription = stringResource(id = R.string.tournament_registration_deadline),
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = viewModel.formatDate(tournament.startDate),
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                    }
                                    val leadedTeam = state.leadedTeam
                                    if (leadedTeam != null) {
                                        val teamIsJoined = tournament.registeredTeams.contains(leadedTeam.name)
                                        if (teamIsJoined) {
                                            SkylexButton(
                                                text = stringResource(id = R.string.button_leave_tournament),
                                                onClick = { viewModel.leaveFromTournament(tournament) },
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            )
                                        } else if (tournament.registeredTeams.size < tournament.maxTeamsCount) {
                                            SkylexButton(
                                                text = stringResource(id = R.string.button_join_tournament),
                                                onClick = { viewModel.joinToTournament(tournament) },
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.clickable(onClick = {
                                viewModel.openTournamentDetails(tournament)
                            })
                        )
                        if (state.tournaments.last() != tournament) {
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}