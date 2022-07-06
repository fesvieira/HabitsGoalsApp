package com.fesvieira.habitsgoals

import android.os.Bundle
import android.widget.Space
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.ui.theme.Blue500
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import com.fesvieira.habitsgoals.ui.theme.black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val habitsList = listOf(
                Habit("Drink 3 L of water", 5),
                Habit("Study Android", 7),
                Habit("Meditate", 8),
                Habit("Read book", 1),
            )
            HabitsGoalsTheme {
                HabitList(habitsList)
            }
        }
    }
}

@Composable
fun HabitList(list: List<Habit>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(list) { item ->
            HabitCard(
                name = item.name,
                info = item.strike)
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
                    .background(Blue500)
                ,
            ){
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

@Preview(
    showBackground = true,
//    widthDp = 360,
//    heightDp = 640
)
@Composable
fun DefaultPreview() {
    val habitsList = listOf(
        Habit("Drink 3 L of water", 5),
        Habit("Study Android", 7),
        Habit("Meditate", 8),
        Habit("Read a book", 1),
    )
    HabitsGoalsTheme {
        HabitList(habitsList)
    }
}
