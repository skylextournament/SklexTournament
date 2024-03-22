package com.skylextournament.app.feature.registration.data

import com.skylextournament.app.common.model.Account

sealed class RegistrationState {
    data object Loading : RegistrationState()
    data class Ready(val account: Account) : RegistrationState()
    data class Error(val error: String) : RegistrationState()
    data class Success(val account: Account) : RegistrationState()
}