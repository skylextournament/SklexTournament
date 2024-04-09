package com.skylextournament.app.common.model

import java.io.Serializable

data class Account(
    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val isAdmin: Boolean = false,
    val description: String = "",
) : Serializable