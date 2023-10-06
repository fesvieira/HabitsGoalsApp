package com.fesvieira.habitsgoals.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@Composable
fun AppCheckBox(isChecked: Boolean, modifier: Modifier = Modifier) {
    val tintAnimation by animateColorAsState(targetValue =
        if (isChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        label = "tintAnimation"
    )

    val drawableRes by remember(isChecked) {
        mutableIntStateOf(if (isChecked) R.drawable.ic_check else R.drawable.ic_close)
    }
    AnimatedContent(targetState = drawableRes, label = "drawableRes") {
        Icon(
            painter = painterResource(it),
            contentDescription = null,
            tint = tintAnimation,
            modifier = modifier
        )
    }
}

/** Run on device to see working properly */
@Preview
@Composable
fun PreviewAppCheckBox() {
    var checked by remember { mutableStateOf(true) }
    val composable = @Composable {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            AppCheckBox(
                checked,
                modifier = Modifier.clickable {
                    checked = !checked
                }
                    .size(200.dp)
            )
        }
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        HabitsGoalsTheme {
            composable()
        }
        HabitsGoalsTheme(useDarkTheme = true ) {
            composable()
        }
    }
}