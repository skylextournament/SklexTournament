package com.skylextournament.app.feature.login.ui

import androidx.lifecycle.ViewModel
import com.skylextournament.app.common.model.Account
import com.skylextournament.app.feature.login.data.LoginState
import com.skylextournament.app.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<LoginState>(LoginState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = LoginState.Ready(Account())
    }

    fun login() {
        val account = (state.value as? LoginState.Ready)?.account ?: return
        _state.value = LoginState.Loading
        accountRepository.login(account) { result ->
            _state.value = if (result.isSuccess) {
                LoginState.Success
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun onEmailChange(newValue: String) {
        val currentState = (state.value as? LoginState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(email = newValue.trim()))
    }

    fun onPasswordChange(newValue: String) {
        val currentState = (state.value as? LoginState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(password = newValue))
    }
}