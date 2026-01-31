package com.luffy1705.gym.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedActionButton(
    delay: Int,
    progress: Float,
    text: String,
    outlined: Boolean = false,
    onClick: () -> Unit
) {
    val offsetY by animateDpAsState(
        targetValue = if (progress == 1f) 0.dp else 40.dp,
        animationSpec = tween(600, delayMillis = delay)
    )

    val alpha by animateFloatAsState(
        targetValue = if (progress == 1f) 1f else 0f,
        animationSpec = tween(600, delayMillis = delay)
    )

    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = offsetY.toPx()
                    this.alpha = alpha
                }
        ) {
            Text(text)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = offsetY.toPx()
                    this.alpha = alpha
                }
        ) {
            Text(text)
        }
    }
}
