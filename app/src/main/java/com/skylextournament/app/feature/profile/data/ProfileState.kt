package com.skylextournament.app.feature.profile.data

import com.skylextournament.app.common.model.Account

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Ready(val account: Account) : ProfileState()
    data class Error(val error: String) : ProfileState()
}