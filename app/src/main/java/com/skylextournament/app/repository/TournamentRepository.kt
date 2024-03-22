package com.skylextournament.app.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.skylextournament.app.common.model.Team
import com.skylextournament.app.common.model.Tournament
import java.time.Instant
import javax.inject.Inject

class TournamentRepository @Inject constructor() {
    private val db by lazy { Firebase.firestore }

    fun createTournament(tournament: Tournament, completion: (Result<Unit>) -> Unit) {
        db.collection(TOURNAMENTS_COLLECTION)
            .document(tournament.id)
            .set(tournament.toMap())
            .addOnSuccessListener {
                completion(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                completion(Result.failure(exception))
            }
    }

    fun getTournaments(completion: (Result<List<Tournament>>) -> Unit) {
        db.collection(TOURNAMENTS_COLLECTION).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null) {
                    val tournaments = snapshot.documents.map {
                        Tournament(
                            id = it.get("id").toString(),
                            name = it.get("name") as String,
                            overview = it.get("overview") as String,
                            startDate = Instant.ofEpochMilli(it.get("startDate") as Long),
                            registrationDeadline = Instant.ofEpochMilli(it.get("registrationDeadline") as Long),
                            maxTeamsCount = (it.get("maxTeamsCount") as? Long)?.toInt() ?: 0,
                            registeredTeams = it.get("registeredTeams") as? List<String> ?: emptyList(),
                        )
                    }
                    completion(Result.success(tournaments))
                } else {
                    completion(Result.success(emptyList()))
                }
            } else {
                val exception = task.exception!!
                completion(Result.failure(exception))
            }
        }
    }

    fun joinTeamToTournament(tournament: Tournament, team: Team, completion: (Result<Unit>) -> Unit) {
        if (tournament.registeredTeams.any { it == team.name }) {
            completion(Result.failure(IllegalArgumentException("Team ${team.name} is already joined.")))
        } else {
            val registeredTeams = tournament.registeredTeams.toMutableList().apply { add(team.name) }
            createTournament(tournament.copy(registeredTeams = registeredTeams), completion)
        }
    }

    fun leaveTeamFromTournament(tournament: Tournament, team: Team, completion: (Result<Unit>) -> Unit) {
        if (tournament.registeredTeams.none { it == team.name }) {
            completion(Result.failure(IllegalArgumentException("Can't leave, team ${team.name} is not joined.")))
        } else {
            val registeredTeams = tournament.registeredTeams.toMutableList().apply { remove(team.name) }
            createTournament(tournament.copy(registeredTeams = registeredTeams), completion)
        }
    }

    private fun Tournament.toMap() = mapOf(
        "id" to id,
        "name" to name,
        "overview" to overview,
        "startDate" to startDate.toEpochMilli(),
        "registrationDeadline" to registrationDeadline.toEpochMilli(),
        "maxTeamsCount" to maxTeamsCount,
        "registeredTeams" to registeredTeams,
    )

    private companion object {
        const val TOURNAMENTS_COLLECTION = "tournaments"
    }
}