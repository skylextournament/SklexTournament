package com.skylextournament.app.feature.tournaments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylextournament.app.common.model.Tournament
import com.skylextournament.app.feature.tournaments.data.TournamentsState
import com.skylextournament.app.repository.SessionRepository
import com.skylextournament.app.repository.TeamRepository
import com.skylextournament.app.repository.TournamentRepository
import com.skylextournament.app.ui.common.DATE_TIME_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TournamentsViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<TournamentsState>(TournamentsState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun goToAddTournament() {
        _state.value = TournamentsState.AddTournament(Tournament())
    }

    fun load() = viewModelScope.launch {
        _state.value = TournamentsState.Loading
        tournamentRepository.getTournaments { tournamentsResult ->
            if (tournamentsResult.isSuccess) {
                teamRepository.getTeams { teamsResult ->
                    _state.value = if (teamsResult.isSuccess) {
                        val team = teamsResult.getOrNull()?.firstOrNull { it.leader.email == sessionRepository.getEmail() }
                        TournamentsState.Ready(
                            isAdmin = sessionRepository.getIsAdmin(),
                            tournaments = tournamentsResult.getOrNull() ?: emptyList(),
                            leadedTeam = team,
                        )
                    } else {
                        TournamentsState.Error(teamsResult.exceptionOrNull()?.message ?: "")
                    }
                }
            } else {
                _state.value = TournamentsState.Error(tournamentsResult.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun onTournamentNameChange(newValue: String) {
        val currentState = (state.value as? TournamentsState.AddTournament) ?: return
        _state.value = currentState.copy(tournament = currentState.tournament.copy(name = newValue))
    }

    fun onTournamentOverviewChange(newValue: String) {
        val currentState = (state.value as? TournamentsState.AddTournament) ?: return
        _state.value = currentState.copy(tournament = currentState.tournament.copy(overview = newValue))
    }

    fun onTournamentMaxTeamCountChange(newValue: String) {
        val currentState = (state.value as? TournamentsState.AddTournament) ?: return
        _state.value = currentState.copy(
            tournament = currentState.tournament.copy(maxTeamsCount = newValue.toIntOrNull() ?: 0)
        )
    }

    fun onTournamentStartDateSelected(newValue: Instant) {
        val currentState = (state.value as? TournamentsState.AddTournament) ?: return
        _state.value = currentState.copy(tournament = currentState.tournament.copy(startDate = newValue))
    }

    fun onTournamentRegistrationDeadlineSelected(newValue: Instant) {
        val currentState = (state.value as? TournamentsState.AddTournament) ?: return
        _state.value = currentState.copy(tournament = currentState.tournament.copy(registrationDeadline = newValue))
    }

    fun createTournament() {
        val tournament = (state.value as? TournamentsState.AddTournament)?.tournament ?: return
        _state.value = TournamentsState.Loading
        tournamentRepository.createTournament(tournament) { load() }
    }

    fun formatDate(date: Instant): String =
        DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).withZone(ZoneId.systemDefault()).format(date)

    fun joinToTournament(tournament: Tournament) {
        val team = (state.value as? TournamentsState.Ready)?.leadedTeam ?: return
        _state.value = TournamentsState.Loading
        tournamentRepository.joinTeamToTournament(tournament, team) { load() }
    }

    fun leaveFromTournament(tournament: Tournament) {
        val team = (state.value as? TournamentsState.Ready)?.leadedTeam ?: return
        _state.value = TournamentsState.Loading
        tournamentRepository.leaveTeamFromTournament(tournament, team) { load() }
    }

    fun openTournamentDetails(tournament: Tournament) {
        // TODO open tournament details
    }
}