package com.skylextournament.app.tournaments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.R
import com.skylextournament.app.tournaments.data.TournamentsState
import com.skylextournament.app.ui.common.toUiText

@Composable
fun TournamentsScreen(
    onListItemClick: (Int) -> Unit,
    viewModel: TournamentsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    color = if (state is TournamentsState.Loading || state is TournamentsState.Error) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is TournamentsState.Loading -> CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                is TournamentsState.Error -> Text(
                    text = state.error.toUiText().asString(context)
                )

                is TournamentsState.Result -> {
                    if (state.tournaments.isEmpty()) {
                        Text(text = stringResource(id = R.string.text_empty_tournaments_list))
                    } else {
                        Column {
                            Text(
                                text = stringResource(id = R.string.top_app_bar_title_tournaments),
                                fontSize = 24.sp
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(state.tournaments, key = { tournament -> tournament.id }) { tournament ->
                                    ListItem(
                                        headlineContent = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Circle,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .padding(
                                                            end = 8.dp,
                                                            top = 8.dp,
                                                            bottom = 8.dp
                                                        ),
                                                )
                                                Text(text = tournament.name)
                                            }
                                        },
                                        supportingContent = {
                                            Text(
                                                text = tournament.name
                                            )
                                        },
                                        modifier = Modifier.clickable(onClick = {
                                            onListItemClick(
                                                tournament.id
                                            )
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
        }
    }
}