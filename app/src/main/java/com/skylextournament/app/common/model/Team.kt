package com.skylextournament.app.common.model

import java.io.Serializable

data class Team(
    val name: String = "",
    val leader: Account = Account(),
    val maxMembersCount: Int = 0,
    val teamMembers: List<Account> = emptyList(),
    val invitedMembers: List<Account> = emptyList(),
) : Serializable
