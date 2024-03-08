package com.skylextournament.app.tournaments.ui

import androidx.lifecycle.ViewModel
import com.skylextournament.app.tournaments.data.Tournament
import com.skylextournament.app.tournaments.data.TournamentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TournamentsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<TournamentsState>(TournamentsState.Loading)
    val state = _state.asStateFlow()

    init {
        _state.value = TournamentsState.Result(
            tournaments = listOf(
                Tournament(name = "Leauge of Legends Hungary"),
                Tournament(name = "World of Warcraft Hungary"),
                Tournament(name = "World of Tanks Championship"),
                Tournament(name = "Leauge of Legends Hungary"),
                Tournament(name = "World of Warcraft Hungary"),
                Tournament(name = "World of Tanks Championship"),
                Tournament(name = "Leauge of Legends Hungary"),
                Tournament(name = "World of Warcraft Hungary"),
                Tournament(name = "World of Tanks Championship"),
            )
        )
    }
}