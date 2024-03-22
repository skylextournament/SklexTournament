package com.skylextournament.app.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skylextournament.app.R

@Composable
fun SkylexLoader() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SkylexError(
    errorText: String,
    onReloadClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.label_error),
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
        Text(text = errorText)
        SkylexButton(
            text = stringResource(id = R.string.button_reload),
            onClick = {
                onReloadClick()
            },
            modifier = Modifier.width(TextFieldDefaults.MinWidth)
        )
    }
}