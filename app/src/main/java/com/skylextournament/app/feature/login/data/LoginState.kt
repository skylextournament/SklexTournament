package com.skylextournament.app.feature.login.data

import com.skylextournament.app.common.model.Account

sealed class LoginState {
    data object Loading : LoginState()
    data class Ready(val account: Account) : LoginState()
    data class Error(val error: String) : LoginState()
    data object Success : LoginState()
}