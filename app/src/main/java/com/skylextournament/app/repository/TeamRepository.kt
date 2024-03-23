package com.skylextournament.app.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.skylextournament.app.common.model.Account
import com.skylextournament.app.common.model.Team
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val accountRepository: AccountRepository,
) {
    private val db by lazy { Firebase.firestore }

    fun createTeam(team: Team, completion: (Result<Unit>) -> Unit) {
        val leader = Account(
            email = sessionRepository.getEmail(),
            nickname = sessionRepository.getUsername(),
        )
        val newTeam = team.copy(
            leader = leader,
            teamMembers = listOf(leader)
        )
        db.collection(TEAMS_COLLECTION)
            .document(team.name)
            .set(newTeam.toMap())
            .addOnSuccessListener {
                completion(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                completion(Result.failure(exception))
            }
    }

    fun getTeams(completion: (Result<List<Team>>) -> Unit) {
        db.collection(TEAMS_COLLECTION).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null) {
                    val teams = snapshot.documents.map {
                        Team(
                            name = it.get("name") as String,
                            leader = Account(
                                nickname = (it.get("leader") as HashMap<String, String>)["nickname"] as String,
                                email = (it.get("leader") as HashMap<String, String>)["email"] as String,
                            ),
                            maxMembersCount = (it.get("maxMembersCount") as Long).toInt(),
                            teamMembers = (it.get("teamMembers") as ArrayList<Any?>?)?.mapNotNull { member ->
                                (member as? HashMap<String, String>)?.let {
                                    Account(
                                        nickname = member["nickname"] as String,
                                        email = member["email"] as String,
                                    )
                                }
                            } ?: emptyList(),
                            invitedMembers = (it.get("invitedMembers") as ArrayList<Any?>?)?.mapNotNull { member ->
                                (member as? HashMap<String, String>)?.let {
                                    Account(
                                        nickname = member["nickname"] as String,
                                        email = member["email"] as String,
                                    )
                                }
                            } ?: emptyList(),
                        )
                    }
                    completion(Result.success(teams))
                } else {
                    completion(Result.success(emptyList()))
                }
            } else {
                val exception = task.exception!!
                completion(Result.failure(exception))
            }
        }
    }

    fun joinToTeam(team: Team, completion: (Result<Unit>) -> Unit) {
        if (team.teamMembers.size < team.maxMembersCount) {
            val email = sessionRepository.getEmail()
            team.invitedMembers.firstOrNull { it.email == email }?.let { newMember ->
                val teamMembers = team.teamMembers.toMutableList().apply {
                    add(newMember)
                }
                val invitedMembers = team.teamMembers.toMutableList().apply {
                    remove(newMember)
                }
                val joinedTeam = team.copy(
                    teamMembers = teamMembers,
                    invitedMembers = invitedMembers,
                )
                db.collection(TEAMS_COLLECTION)
                    .document(team.name)
                    .set(joinedTeam.toMap())
                    .addOnSuccessListener {
                        completion(Result.success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        completion(Result.failure(exception))
                    }
            }
                ?: completion(Result.failure(IllegalArgumentException("Cannot join to ${team.name}. You're not invited to this team.")))
        } else {
            completion(Result.failure(IllegalArgumentException("Cannot join to ${team.name}. Team has max members.")))
        }
    }

    fun leaveTeam(team: Team, completion: (Result<Unit>) -> Unit) {
        val email = sessionRepository.getEmail()
        team.teamMembers.firstOrNull { it.email == email }?.let { member ->
            val teamMembers = team.teamMembers.toMutableList().apply {
                remove(member)
            }
            val leftTeam = team.copy(teamMembers = teamMembers)
            if (team.leader.email == email) {
                db.collection(TEAMS_COLLECTION)
                    .document(team.name)
                    .delete()
                    .addOnSuccessListener {
                        completion(Result.success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        completion(Result.failure(exception))
                    }
            } else {
                db.collection(TEAMS_COLLECTION)
                    .document(team.name)
                    .set(leftTeam.toMap())
                    .addOnSuccessListener {
                        completion(Result.success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        completion(Result.failure(exception))
                    }
            }
        }
    }

    fun inviteToTeam(team: Team, email: String, completion: (Result<Unit>) -> Unit) {
        accountRepository.getAccount(email) { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { newMember ->
                    when {
                        team.teamMembers.any { it.email == newMember.email } -> {
                            completion(Result.failure(IllegalArgumentException("${newMember.nickname} is already a member of the ${team.name} team")))
                        }

                        team.invitedMembers.any { it.email == newMember.email } -> {
                            completion(Result.failure(IllegalArgumentException("${newMember.nickname} is already invited to ${team.name} team")))
                        }

                        else -> {
                            val invitedMembers = team.invitedMembers.toMutableList().apply {
                                add(newMember)
                            }
                            val invitedTeam = team.copy(invitedMembers = invitedMembers)
                            db.collection(TEAMS_COLLECTION)
                                .document(team.name)
                                .set(invitedTeam.toMap())
                                .addOnSuccessListener {
                                    completion(Result.success(Unit))
                                }
                                .addOnFailureListener { exception ->
                                    completion(Result.failure(exception))
                                }
                        }
                    }
                }
            } else {
                completion(Result.failure(IllegalArgumentException("Account $email no exists")))
            }
        }
    }

    private fun Team.toMap() = mapOf(
        "name" to name,
        "leader" to leader,
        "maxMembersCount" to maxMembersCount,
        "teamMembers" to teamMembers,
        "invitedMembers" to invitedMembers,
    )

    private companion object {
        const val TEAMS_COLLECTION = "teams"
    }
}
