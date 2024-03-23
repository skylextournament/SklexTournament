package com.skylextournament.app.feature.team.ui

import androidx.lifecycle.ViewModel
import com.skylextournament.app.common.model.Team
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.repository.SessionRepository
import com.skylextournament.app.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val teamsRepository: TeamRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<TeamState>(TeamState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = TeamState.Loading
        teamsRepository.getTeams { result ->
            _state.value = if (result.isSuccess) {
                val teams = result.getOrNull() ?: emptyList()
                val email = sessionRepository.getEmail()
                val team = teams.firstOrNull { team ->
                    team.teamMembers.any { it.email == email }
                }
                if (team != null) {
                    TeamState.HasTeam(
                        team = team,
                        isLeader = email == team.leader.email,
                    )
                } else {
                    val myInvitedTeams = teams.filter { it.invitedMembers.any { it.email == email } }
                    TeamState.NoTeam(myInvitedTeams = myInvitedTeams)
                }
            } else {
                TeamState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun goToAddTeam() {
        _state.value = TeamState.AddTeam(Team())
    }

    fun createTeam() {
        val team = (state.value as? TeamState.AddTeam)?.team ?: return
        _state.value = TeamState.Loading
        teamsRepository.createTeam(team) { result ->
            if (result.isSuccess) {
                load()
            } else {
                _state.value = TeamState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun joinToTeam(team: Team) {
        _state.value = TeamState.Loading
        teamsRepository.joinToTeam(team) { result ->
            if (result.isSuccess) {
                load()
            } else {
                _state.value = TeamState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun inviteToTeam() {
        val currentState = (state.value as? TeamState.HasTeam) ?: return
        _state.value = TeamState.Loading
        teamsRepository.inviteToTeam(currentState.team, currentState.invitedEmail) { result ->
            if (result.isSuccess) {
                load()
            } else {
                _state.value = TeamState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun leaveTeam() {
        val currentState = (state.value as? TeamState.HasTeam) ?: return
        _state.value = TeamState.Loading
        teamsRepository.leaveTeam(currentState.team) { result ->
            if (result.isSuccess) {
                load()
            } else {
                _state.value = TeamState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun onTeamNameChange(newValue: String) {
        val currentState = (state.value as? TeamState.AddTeam) ?: return
        _state.value = currentState.copy(team = currentState.team.copy(name = newValue))
    }

    fun onTeamMaxMembersChange(newValue: String) {
        val currentState = (state.value as? TeamState.AddTeam) ?: return
        _state.value = currentState.copy(team = currentState.team.copy(maxMembersCount = newValue.toIntOrNull() ?: 0))
    }

    fun onInvitedEmailChange(newValue: String) {
        val currentState = (state.value as? TeamState.HasTeam) ?: return
        _state.value = currentState.copy(invitedEmail = newValue)
    }
}