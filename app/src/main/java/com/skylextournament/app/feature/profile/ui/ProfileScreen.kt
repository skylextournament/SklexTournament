package com.skylextournament.app.feature.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skylextournament.app.R
import com.skylextournament.app.feature.profile.data.ProfileState
import com.skylextournament.app.ui.common.SkylexButton
import com.skylextournament.app.ui.common.SkylexError
import com.skylextournament.app.ui.common.SkylexLoader

@Composable
fun ProfileScreen(
    navigateToLogin: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = stringResource(id = R.string.top_app_bar_title_profile))
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                ProfileState.Loading -> SkylexLoader()

                is ProfileState.Ready -> Scaffold(
                    bottomBar = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            SkylexButton(
                                text = stringResource(id = R.string.button_logout),
                                onClick = {
                                    viewModel.logout(navigateToLogin)
                                },
                                modifier = Modifier.width(TextFieldDefaults.MinWidth)
                            )
                        }
                    }
                ) { padding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.field_hello)} ${state.account.nickname}!",
                            Modifier
                                .padding(24.dp)
                        )
                    }
                }

                is ProfileState.Error -> SkylexError(errorText = state.error) { viewModel.load() }
            }
        }
    }
}