package com.fesvieira.habitsgoals.ui.components.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import com.fesvieira.habitsgoals.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerPickerDialog(
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onOkayClick: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .shadow(10.dp, shape = RoundedCornerShape(32.dp))
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(32.dp)
                )
                .padding(16.dp)
        ) {
            TimePicker(
                state = timePickerState,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(start = if (timePickerState.is24hour) 16.dp else 0.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = Typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onOkayClick() }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.cancel),
                    style = Typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDismiss() }
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewTimerPickerDialog() {
    HabitsGoalsTheme {
        TimerPickerDialog(rememberTimePickerState(is24Hour = false), onOkayClick = {}, onDismiss = {})
    }
}