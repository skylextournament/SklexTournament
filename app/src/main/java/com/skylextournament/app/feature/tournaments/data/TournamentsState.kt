package com.skylextournament.app.feature.tournaments.data

import com.skylextournament.app.common.model.Team
import com.skylextournament.app.common.model.Tournament

sealed class TournamentsState {
    data object Loading : TournamentsState()
    data class Error(val error: String) : TournamentsState()
    data class Ready(
        val tournaments: List<Tournament>,
        val isAdmin: Boolean,
        val leadedTeam: Team?,
    ) : TournamentsState()

    data class AddTournament(val tournament: Tournament) : TournamentsState()
}