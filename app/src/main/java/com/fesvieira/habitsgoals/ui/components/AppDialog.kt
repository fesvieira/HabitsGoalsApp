package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import com.fesvieira.habitsgoals.ui.theme.Typography
import com.fesvieira.habitsgoals.ui.theme.md_theme_dark_tertiary
import com.fesvieira.habitsgoals.ui.theme.md_theme_light_secondary

@Composable
fun AppDialog(
    yesCallback: () -> Unit,
    noCallback: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = isPreview)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(md_theme_light_secondary, RoundedCornerShape(32.dp))
                .padding(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_alert),
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Delete Habit?",
                color = Color.White,
                style = Typography.h1,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Are you sure you want to delete this habit? This action can not be undone",
                color = Color.White,
                style = Typography.body2,
                textAlign = TextAlign.Center
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_dark_tertiary,
                        contentColor = Color.Black,
                        disabledContainerColor = md_theme_dark_tertiary,
                        disabledContentColor = Color.White,
                    ),
                    onClick = { /*TODO*/ }) {
                    Text(text = "Yes", style = Typography.button)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_dark_tertiary,
                        contentColor = Color.Black,
                        disabledContainerColor = md_theme_dark_tertiary,
                        disabledContentColor = Color.White,
                    ),
                    onClick = { /*TODO*/ }) {
                    Text(text = "No", style = Typography.button)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAppDialog() {
    HabitsGoalsTheme {
        AppDialog({}, {})
    }
}