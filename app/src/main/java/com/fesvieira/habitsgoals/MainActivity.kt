package com.fesvieira.habitsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fesvieira.habitsgoals.HabitRepository.habitsList
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
fun MainScreen() {
    HabitsGoalsTheme {
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
            Navigation()
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
