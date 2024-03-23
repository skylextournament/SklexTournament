package com.skylextournament.app.feature.team.data

import com.skylextournament.app.common.model.Team

sealed class TeamState {
    data object Loading : TeamState()
    data class Error(val error: String) : TeamState()
    data class HasTeam(
        val team: Team,
        val isLeader: Boolean,
        val invitedEmail: String = "",
    ) : TeamState()

    data class NoTeam(
        val myInvitedTeams: List<Team>,
    ) : TeamState()

    data class AddTeam(
        val team: Team
    ) : TeamState()
}