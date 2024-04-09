package com.skylextournament.app.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylextournament.app.feature.profile.data.ProfileState
import com.skylextournament.app.repository.AccountRepository
import com.skylextournament.app.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun load() = viewModelScope.launch {
        _state.value = ProfileState.Loading
        accountRepository.getAccount(sessionRepository.getEmail()) { result ->
            _state.value = if (result.isSuccess) {
                result.getOrNull()?.let { ProfileState.Ready(it) } ?: ProfileState.Error("No account")
            } else {
                ProfileState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun logout(navigateToLogin: () -> Unit) {
        _state.value = ProfileState.Loading
        accountRepository.logout()
        navigateToLogin()
    }

    fun onDescriptionChange(newValue: String) {
        val currentState = (state.value as? ProfileState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(description = newValue))
    }

    fun saveDescription() {
        val currentState = (state.value as? ProfileState.Ready) ?: return
        _state.value = ProfileState.Loading
        accountRepository.updateAccount(currentState.account) { result ->
            if (result.isSuccess) {
                load()
            } else {
                _state.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }
}