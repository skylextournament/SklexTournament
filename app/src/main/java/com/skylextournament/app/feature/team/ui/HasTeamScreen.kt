package com.skylextournament.app.feature.team.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skylextournament.app.R
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.ui.common.NormalTextField
import com.skylextournament.app.ui.common.SkylexButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasTeamScreen(state: TeamState.HasTeam, viewModel: TeamViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_your_team)) },
            )
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                text = state.team.name,
            )
            Text(
                text = stringResource(id = R.string.label_team_members),
                modifier = Modifier.padding(start = 8.dp, top = 16.dp),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                state.team.teamMembers.forEach { member ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = member.nickname)
                        if (member.email == state.team.leader.email) {
                            Text(
                                text = stringResource(id = R.string.label_team_members_leader),
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            if (state.isLeader) {
                if (state.team.invitedMembers.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.label_no_invited_member),
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.label_invited_members),
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    ) {
                        state.team.invitedMembers.forEach { member ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = member.nickname)
                                Text(text = member.email)
                            }
                        }
                    }
                }
                Row(modifier = Modifier.padding(8.dp)) {
                    NormalTextField(
                        value = state.invitedEmail,
                        label = stringResource(id = R.string.textfiekd_invite_team_member),
                        singleLine = false,
                        onValueChange = { newValue ->
                            viewModel.onInvitedEmailChange(newValue)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        onDone = { }
                    )
                    SkylexButton(
                        text = stringResource(id = R.string.button_invite_team_member),
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            viewModel.inviteToTeam()
                        },
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                SkylexButton(
                    modifier = Modifier.padding(top = 36.dp),
                    text = stringResource(id = R.string.button_leave_team),
                    onClick = {
                        viewModel.leaveTeam()
                    },
                )
            }
        }
    }
}