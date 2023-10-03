package com.fesvieira.habitsgoals.ui.components.calendar

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import java.time.LocalDate
import java.util.Locale

@Composable
fun CalendarComponent(
    baseDate: LocalDate,
    modifier: Modifier = Modifier
) {
    var dateShift by remember {
        mutableIntStateOf(0)
    }

    val date by remember(dateShift) {
        derivedStateOf {
            when {
                dateShift > 0 -> baseDate.plusMonths(dateShift.toLong())
                dateShift < 0 -> baseDate.minusMonths(dateShift.toLong())
                else -> baseDate
            }
        }
    }
    val daysOfWeek by remember {
        mutableStateOf(listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"))
    }

    val currentDay by remember {
        derivedStateOf { baseDate.dayOfMonth }
    }

    val monthLength by remember {
        derivedStateOf { date.month.length(date.isLeapYear) }
    }

    val skipWeekDays by remember {
        derivedStateOf {
            val weekDay = date.minusDays(date.dayOfMonth.toLong() - 1).dayOfWeek.value

            if (weekDay == 7) 0
            else weekDay
        }
    }

    LazyVerticalGrid(
        columns = Fixed(7),
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "${date.month.name.lowercase().replaceFirstChar { it.uppercase() }}, ${date.year}",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp).clickable {
                        dateShift -= 1
                    }
                )

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp).clickable {
                        dateShift += 1
                    }
                )
            }

        }
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

        items(skipWeekDays) {
            Box(modifier = Modifier.size(40.dp))
        }

        items(monthLength) {
            CalendarDay(
                day = it + 1,
                isCurrentDay = it + 1 == currentDay && dateShift == 0,
                isSelected = false
            )
        }
    }
}

@Composable
private fun CalendarDay(day: Int, isCurrentDay: Boolean, isSelected: Boolean) {

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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewCalendarComponent() {
    HabitsGoalsTheme {
        CalendarComponent(
            baseDate = LocalDate.of(2023, 3, 1)
        )
    }
}