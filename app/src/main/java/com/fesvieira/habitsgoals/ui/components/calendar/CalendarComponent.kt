package com.fesvieira.habitsgoals.ui.components.calendar

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@Composable
fun CalendarComponent() {
    val daysOfWeek by remember {
        mutableStateOf(listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"))
    }

    LazyVerticalGrid(
        columns = Fixed(7),
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        items(daysOfWeek) { day ->
            Text(
                text = day,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(31) {
            CalendarDay(
                day = it + 1,
                isCurrentDay = it == 18,
                isSelected = true
            )
        }
    }
}

@Composable
fun CalendarDay(day: Int, isCurrentDay: Boolean, isSelected: Boolean) {

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.inversePrimary
            isCurrentDay -> MaterialTheme.colorScheme.background
            else -> MaterialTheme.colorScheme.secondaryContainer
        },
        label = "backgroundColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .size(40.dp)
            .padding(vertical = 1.dp, horizontal = 6.dp)
            .clip(CircleShape)
            .background(backgroundColor)

    ) {
        Text(
            text = day.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewCalendarComponent() {
    HabitsGoalsTheme {
        CalendarComponent()
    }
}