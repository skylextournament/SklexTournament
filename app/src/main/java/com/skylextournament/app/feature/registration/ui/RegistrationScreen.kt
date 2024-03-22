package com.skylextournament.app.feature.registration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.R
import com.skylextournament.app.feature.registration.data.RegistrationState
import com.skylextournament.app.ui.common.NormalTextField
import com.skylextournament.app.ui.common.PasswordTextField
import com.skylextournament.app.ui.common.SkylexButton
import com.skylextournament.app.ui.common.SkylexError
import com.skylextournament.app.ui.common.SkylexLoader

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    var isEmailFormatError by remember { mutableStateOf(false) }

    var isNicknameError by remember { mutableStateOf(false) }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = stringResource(id = R.string.registration_title))
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is RegistrationState.Loading -> SkylexLoader()

                is RegistrationState.Error -> SkylexError(errorText = state.error) { viewModel.back() }

                is RegistrationState.Ready -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        NormalTextField(
                            value = state.account.email,
                            label = stringResource(id = R.string.textfield_label_email),
                            onValueChange = { newValue ->
                                viewModel.onEmailChange(newValue)
                                isEmailFormatError = false
                            },
                            isError = isEmailFormatError,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                            },
                            onDone = { }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        NormalTextField(
                            value = state.account.nickname,
                            label = stringResource(id = R.string.textfield_label_nickname),
                            onValueChange = { newValue ->
                                viewModel.onNicknameChange(newValue)
                                isNicknameError = false
                            },
                            isError = isNicknameError,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )
                            },
                            onDone = { }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordTextField(
                            value = state.account.password,
                            label = stringResource(id = R.string.textfield_label_password),
                            onValueChange = { newValue ->
                                viewModel.onPasswordChange(newValue)
                                isPasswordError = false
                            },
                            isError = isPasswordError,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null
                                )
                            },
                            isVisible = isPasswordVisible,
                            onVisibilityChanged = { isPasswordVisible = !isPasswordVisible },
                            onDone = { }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        SkylexButton(
                            text = stringResource(id = R.string.button_label_register),
                            onClick = {
                                when {
                                    state.account.nickname.isEmpty() -> isNicknameError = true
                                    state.account.email.isEmpty() || !viewModel.isValidEmail() -> isEmailFormatError = true
                                    state.account.password.isEmpty() -> isPasswordError = true
                                    else -> {
                                        viewModel.createAccount()
                                    }
                                }
                            },
                        )
                    }
                }

                is RegistrationState.Success -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(id = R.string.label_register_success))
                        Spacer(modifier = Modifier.height(10.dp))
                        SkylexButton(
                            text = stringResource(id = R.string.button_navigate_home),
                            onClick = {
                                viewModel.login(navigateToHome)
                            },
                        )
                    }
                }
            }
        }
    }
}

