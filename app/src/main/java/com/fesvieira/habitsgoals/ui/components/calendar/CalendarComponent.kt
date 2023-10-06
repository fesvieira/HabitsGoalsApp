package com.fesvieira.habitsgoals.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.helpers.toStamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun CalendarComponent(
    baseDate: LocalDate,
    daysDone: List<Long>,
    onToggleDay: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
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
    val daysOfWeek = remember {
        listOf(
            DayOfWeek.SUNDAY.name,
            DayOfWeek.MONDAY.name,
            DayOfWeek.TUESDAY.name,
            DayOfWeek.WEDNESDAY.name,
            DayOfWeek.THURSDAY.name,
            DayOfWeek.FRIDAY.name,
            DayOfWeek.SATURDAY.name,
        )
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
    var rightArrowScale by remember { mutableFloatStateOf(1.0f) }
    var leftArrowScale by remember { mutableFloatStateOf(1.0f) }
    val rightArrowScaleAnim by animateFloatAsState(
        targetValue = rightArrowScale,
        label = "rightArrowScaleAnim"
    )
    val leftArrowScaleAnim by animateFloatAsState(
        targetValue = leftArrowScale,
        label = "leftArrowScaleAnim"
    )

    LazyVerticalGrid(
        columns = Fixed(7),
        modifier = modifier
            .height(if (monthLength == 31 && skipWeekDays > 4) 400.dp else 350.dp)
            .widthIn(max = 400.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), true)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "${
                        date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
                    }, ${date.year}",
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
                    modifier = Modifier
                        .size(24.dp)
                        .scale(leftArrowScaleAnim)
                        .clip(CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                dateShift -= 1
                                leftArrowScale = 1.4f
                                delay(200)
                                leftArrowScale = 1.0f
                            }
                        }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .scale(rightArrowScaleAnim)
                        .clip(CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                dateShift += 1
                                rightArrowScale = 1.4f
                                delay(200)
                                rightArrowScale = 1.0f
                            }
                        }
                )
            }

        }
        items(daysOfWeek) { day ->
            Text(
                text = day.take(3),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
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
                isSelected = daysDone.contains(
                    LocalDate.of(
                        date.year,
                        date.month.value,
                        it + 1
                    ).toStamp
                ),
                onClick = {
                    onToggleDay(LocalDate.of(date.year, date.month.value, it + 1).toStamp)
                }
            )
        }
    }
}

@Composable
private fun CalendarDay(
    day: Int,
    isCurrentDay: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.inversePrimary
            isCurrentDay -> MaterialTheme.colorScheme.background
            else -> MaterialTheme.colorScheme.secondaryContainer
        },
        animationSpec = tween(400),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isCurrentDay && isCurrentDay) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.secondaryContainer,
        animationSpec = tween(400),
        label = "backgroundColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(vertical = 3.dp)
            .size(40.dp)
            .padding(vertical = 1.dp, horizontal = 6.dp)
            .clip(CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .background(backgroundColor)
            .clickable {
                onClick()
            }

    ) {
        Text(
            text = day.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun Preview() {
    Column(modifier = Modifier.fillMaxSize()) {
        CalendarComponent(
            baseDate = LocalDate.of(2023, 10, 5),
            daysDone = emptyList(),
            onToggleDay = {})
    }
}