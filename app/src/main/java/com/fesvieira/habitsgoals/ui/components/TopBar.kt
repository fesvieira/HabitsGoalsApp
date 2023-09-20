package com.fesvieira.habitsgoals.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor =  MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Color.White
            )
        }
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    HabitsGoalsTheme {
        TopBar(title = "Testing")
    }    
}