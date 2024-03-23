package com.skylextournament.app.feature.team.ui

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skylextournament.app.R
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.ui.common.SkylexButton

@Composable
fun NoTeamScreen(state: TeamState.NoTeam, viewModel: TeamViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.goToAddTeam() },
                icon = { Icon(Icons.Filled.Add, stringResource(id = R.string.button_add_team)) },
                text = { Text(text = stringResource(id = R.string.button_add_team)) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.top_app_bar_title_team),
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
            Text(
                text = stringResource(id = R.string.label_no_team_description),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            )
            if (state.myInvitedTeams.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.label_no_invited_team),
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                )
            } else {
                Text(
                    text = stringResource(id = R.string.label_invited_teams),
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.myInvitedTeams, key = { team -> team.name }) { team ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = team.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Column {
                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Column {
                                            Text(text = stringResource(id = R.string.label_team_leader))
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = stringResource(id = R.string.label_team_leader),
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = team.leader.nickname,
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(36.dp))
                                        SkylexButton(
                                            text = stringResource(id = R.string.button_team_join),
                                            onClick = {
                                                viewModel.joinToTeam(team)
                                            },
                                            modifier = Modifier.width(TextFieldDefaults.MinWidth)
                                        )
                                    }
                                }
                            },
                        )
                        if (state.myInvitedTeams.last() != team) {
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