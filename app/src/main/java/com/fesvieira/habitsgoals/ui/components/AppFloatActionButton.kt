package com.fesvieira.habitsgoals.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesvieira.habitsgoals.R

@Composable
fun AppFloatActionButton(icon: Painter, onClick: () -> Unit) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Icon(
            painter = icon,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            contentDescription = stringResource(R.string.save_icon),
        )
    }
}