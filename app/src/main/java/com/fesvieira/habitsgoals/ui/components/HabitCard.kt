package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitCard(
    habit: Habit,
    onClickListener: () -> Unit,
    onAddClickListener: () -> Unit
) {
    HabitsGoalsTheme {
        Card(
            elevation = 6.dp,
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp)),
            onClick = onClickListener
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = habit.name,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(
                            R.string.current_strike,
                            habit.strike.toString(),
                            habit.goal.toString()
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                IconButton(
                    onClick = { onAddClickListener() },
                    Modifier
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = stringResource(R.string.add_icon),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHabitCardAdapter() {
    HabitCard(
        habit = Habit("Meditate", 0, 0),
        onClickListener = {},
        onAddClickListener = {},
    )
}