package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(title: String) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 16.dp),
            letterSpacing = 2.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}