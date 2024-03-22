package com.skylextournament.app.feature.registration.ui

import androidx.lifecycle.ViewModel
import com.skylextournament.app.common.model.Account
import com.skylextournament.app.feature.registration.data.RegistrationState
import com.skylextournament.app.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Loading)
    val state = _state.asStateFlow()

    init {
        _state.value = RegistrationState.Ready(Account())
    }

    fun createAccount() {
        val account = (state.value as? RegistrationState.Ready)?.account ?: return
        _state.value = RegistrationState.Loading
        accountRepository.createAccount(account) { result ->
            _state.value = if (result.isSuccess) {
                RegistrationState.Success(account)
            } else {
                RegistrationState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }

    fun onEmailChange(newValue: String) {
        val currentState = (state.value as? RegistrationState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(email = newValue.trim()))
    }

    fun onNicknameChange(newValue: String) {
        val currentState = (state.value as? RegistrationState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(nickname = newValue))
    }

    fun onPasswordChange(newValue: String) {
        val currentState = (state.value as? RegistrationState.Ready) ?: return
        _state.value = currentState.copy(account = currentState.account.copy(password = newValue))
    }

    fun isValidEmail(): Boolean {
        val emailValue = (state.value as? RegistrationState.Ready)?.account?.email ?: return false
        val emailRegex =
            Regex(
                "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])",
            )
        return emailRegex.matches(emailValue.lowercase())
    }

    fun back() {
        _state.value = RegistrationState.Ready(Account())
    }

    fun login(navigateToHome: () -> Unit) {
        val account = (state.value as? RegistrationState.Success)?.account ?: return
        _state.value = RegistrationState.Loading
        accountRepository.login(account) { result ->
            if (result.isSuccess) {
                navigateToHome()
            } else {
                _state.value = RegistrationState.Error(result.exceptionOrNull()?.message ?: "")
            }
        }
    }
}