package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.Typography
import com.fesvieira.habitsgoals.ui.theme.md_theme_dark_primaryContainer

@Composable
fun DeleteHabitSnackbar(
    snackbarHostState: SnackbarHostState,
    habitName: String,
    onActionClick: () -> Unit
) {
    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(
            modifier = Modifier
                .padding(12.dp),
            backgroundColor = md_theme_dark_primaryContainer,
            contentColor = Color.White,
            elevation = 20.dp,
            action = {
                Text(
                    text = stringResource(R.string.undo),
                    style = Typography.button,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            it.dismiss()
                            onActionClick()
                        }
                )
            }
        ) {
            Text(
                text = stringResource(R.string.deleted_x, habitName),
                style = Typography.body2
            )
        }
    }
}