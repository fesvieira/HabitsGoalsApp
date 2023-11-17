package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesvieira.habitsgoals.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppFloatActionButton(
    icon: Painter,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isDebouncing by remember { mutableStateOf(false) }
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp),
        onClick = {
            if (isEnabled && !isDebouncing) {
                coroutineScope.launch {
                    isDebouncing = true
                    delay(1000)
                    isDebouncing = false
                }
                onClick()
            }
        }
    ) {
        if (!isLoading) {
            Icon(
                painter = icon,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                contentDescription = stringResource(R.string.save_icon),
            )
        } else {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

    }
}