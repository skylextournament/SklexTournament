package com.skylextournament.app.tournaments.data

import kotlin.random.Random

sealed class TournamentsState {
    data object Loading : TournamentsState()
    data class Error(val error: Throwable) : TournamentsState()
    data class Result(val tournaments: List<Tournament>) : TournamentsState()
}

data class Tournament(
    val id: Int = Random.nextInt(),
    val name: String
)