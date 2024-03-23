package com.skylextournament.app.feature.team.ui

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
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skylextournament.app.R
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.ui.common.NormalTextField
import com.skylextournament.app.ui.common.SkylexButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamScreen(state: TeamState.AddTeam, viewModel: TeamViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_create_team)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.load() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate Back")
                    }
                }
            )
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            NormalTextField(
                value = state.team.name,
                label = stringResource(id = R.string.textfield_name_of_the_team),
                singleLine = false,
                onValueChange = { newValue ->
                    viewModel.onTeamNameChange(newValue)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.FormatColorText,
                        contentDescription = null
                    )
                },
                onDone = { }
            )
            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(
                value = state.team.maxMembersCount.toString(),
                label = stringResource(id = R.string.textfield_max_members),
                onValueChange = { newValue ->
                    viewModel.onTeamMaxMembersChange(newValue)
                },
                keyboardType = KeyboardType.Number,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.SupervisorAccount,
                        contentDescription = null
                    )
                },
                onDone = { viewModel.createTeam() }
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                SkylexButton(
                    text = stringResource(id = R.string.title_create_team),
                    onClick = {
                        viewModel.createTeam()
                    },
                )
            }
        }
    }
}