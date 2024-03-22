package com.skylextournament.app.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skylextournament.app.ui.common.BottomNavigationBar

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BottomNavigationBar(navigateToLogin)
    }
}