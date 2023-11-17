package com.fesvieira.habitsgoals.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fesvieira.habitsgoals.ui.components.calendar.CalendarComponent
import java.time.LocalDate

@Composable
fun CalendarDialog(
    baseDate: LocalDate,
    daysDone: List<Long>,
    onToggleDay: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        CalendarComponent(
            baseDate = baseDate,
            daysDone = daysDone,
            onToggleDay = onToggleDay,
            modifier = modifier
                .fillMaxWidth()
        )
    }
}