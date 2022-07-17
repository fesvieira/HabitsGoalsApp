package com.fesvieira.habitsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.ui.theme.Blue500
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun HabitCard(name: String, info: Int) {
    Card(
        elevation = 6.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
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

@Composable
fun HabitList(list: List<Habit>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items(list) { item ->
            HabitCard(
                name = item.name,
                info = item.strike
            )
        }
    }
}

@Composable
fun MainScreen() {
    HabitsGoalsTheme {
        val habitsList = listOf(
            Habit("Drink 3 L of water", 5),
            Habit("Study Android", 7),
            Habit("Meditate", 8),
            Habit("Read a book", 1),
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Blue700,
                ) {
                    Text (
                        text = "Habits List",
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_add,
                        ),
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
            }
        ) {
            HabitList(list = habitsList)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun DefaultPreview() {
    MainScreen()
}
