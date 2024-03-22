package com.skylextournament.app.common.model

import java.io.Serializable
import java.time.Instant
import java.util.UUID

data class Tournament(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val overview: String = "",
    val startDate: Instant = Instant.now(),
    val registrationDeadline: Instant = Instant.now(),
    val maxTeamsCount: Int = 0,
    val registeredTeams: List<String> = emptyList(),
) : Serializable