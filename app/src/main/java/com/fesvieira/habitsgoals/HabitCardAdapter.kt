package com.fesvieira.habitsgoals

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.ui.theme.Blue500
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitCardAdapter(name: String, info: Int, onClickListener: () -> Unit) {

    HabitsGoalsTheme {
        Card(
            elevation = 6.dp,
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
                        text = name
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$info",
                        fontSize = 12.sp
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    Modifier
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(Blue500),
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_add
                        ),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewHabitCardAdapter() {
    HabitCardAdapter("Gerjo", 1) {}
}