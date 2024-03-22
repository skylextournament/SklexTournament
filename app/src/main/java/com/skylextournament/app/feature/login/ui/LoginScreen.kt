package com.skylextournament.app.feature.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.R
import com.skylextournament.app.feature.login.data.LoginState
import com.skylextournament.app.ui.common.NormalTextField
import com.skylextournament.app.ui.common.PasswordTextField
import com.skylextournament.app.ui.common.SkylexButton
import com.skylextournament.app.ui.common.SkylexError
import com.skylextournament.app.ui.common.SkylexLoader
import com.skylextournament.app.ui.theme.gradientBrush

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(topBar = {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 12.dp, end = 12.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                style = TextStyle(
                    brush = gradientBrush,
                    lineBreak = LineBreak.Heading
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                text = stringResource(id = R.string.login_title),
            )
        }
    }) { padding ->
        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                LoginState.Loading -> SkylexLoader()

                is LoginState.Error -> SkylexError(errorText = state.error) { viewModel.load() }

                LoginState.Success -> navigateToHome()

                is LoginState.Ready -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            NormalTextField(
                                value = state.account.email,
                                label = stringResource(id = R.string.textfield_label_email),
                                onValueChange = { newValue ->
                                    viewModel.onEmailChange(newValue)
                                    isEmailError = false
                                },
                                isError = isEmailError,
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
                            Spacer(modifier = Modifier.height(30.dp))
                            SkylexButton(
                                text = stringResource(id = R.string.button_label_login),
                                onClick = {
                                    if (state.account.email.isEmpty()) {
                                        isEmailError = true
                                    } else if (state.account.password.isEmpty()) {
                                        isPasswordError = true
                                    } else {
                                        viewModel.login()
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            SkylexButton(
                                text = stringResource(id = R.string.button_label_registration),
                                onClick = {
                                    onRegisterClick()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}