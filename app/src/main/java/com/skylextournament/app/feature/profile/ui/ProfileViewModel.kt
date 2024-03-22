package com.skylextournament.app.feature.profile.ui

import androidx.lifecycle.ViewModel
import com.skylextournament.app.common.model.Account
import com.skylextournament.app.feature.profile.data.ProfileState
import com.skylextournament.app.repository.AccountRepository
import com.skylextournament.app.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = ProfileState.Ready(
            Account(
                nickname = sessionRepository.getUsername(),
                email = sessionRepository.getEmail(),
            )
        )
    }

    fun logout(navigateToLogin: () -> Unit) {
        _state.value = ProfileState.Loading
        accountRepository.logout()
        navigateToLogin()
    }
}